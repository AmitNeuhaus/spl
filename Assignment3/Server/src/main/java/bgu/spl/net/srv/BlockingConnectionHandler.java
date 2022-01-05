package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.bidi.ConnectionHandler;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final SocketChannel sock;
//    private BufferedInputStream in;
//    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private final Queue<T> writeQueue;

    public BlockingConnectionHandler(SocketChannel sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.writeQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        try (SocketChannel sock = this.sock) { //just for automatic closing
            sock.configureBlocking(false);
            int read = -1;
            ByteBuffer readBuff = ByteBuffer.allocateDirect(1<<13);
            ByteBuffer writeBuffer= ByteBuffer.allocateDirect(1<<13);

            while (!protocol.shouldTerminate() && connected ){
                if ((read = sock.read(readBuff))> 0){
                    readBuff.flip();
                    while(readBuff.hasRemaining()) {
                        T nextMessage = encdec.decodeNextByte(readBuff.get());
                        if (nextMessage != null) {
                            System.out.println("message as received from client-" + nextMessage);
                            protocol.process(nextMessage);
                        }
                    }
                    readBuff.clear();
                }
                if(!writeQueue.isEmpty()){
                    System.out.println("should write response");
                    T msg = writeQueue.poll();
                    byte[] encodedMsg = encdec.encode(msg);
                    writeBuffer.put(encodedMsg);
                    writeBuffer.flip();
                    while(writeBuffer.hasRemaining()){
                        sock.write(writeBuffer);
                    }
                    writeBuffer.clear();
                }

            }
            close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    public void send(T msg) {
        //Todo: Maybe need to synchronize // work with a queue so other clients can insert messages to the queue
        // and only the current handler will pop msgs from the queue and write to the buffer.
        writeQueue.add(msg);
        System.out.println("added msg to write Q");
    }
}
