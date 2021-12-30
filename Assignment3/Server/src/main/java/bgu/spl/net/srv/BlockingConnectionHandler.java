package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.bidi.ConnectionHandler;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private final Queue<T> writeQueue;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.writeQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read = -1;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && ((read = in.read()) >= 0) || !writeQueue.isEmpty()){
                if (read >= 0){
                    T nextMessage = encdec.decodeNextByte((byte) read);
                    if (nextMessage != null) {
                        protocol.process(nextMessage);
                    }
                }
                if(!writeQueue.isEmpty()){
                    T msg = writeQueue.poll();
                    byte[] encodedMsg = encdec.encode(msg);
                    out.write(encodedMsg,0,encodedMsg.length);
                    out.flush();
                }

            }

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
