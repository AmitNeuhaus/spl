package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;
    private int connectionCounter = 0;
    private ConnectionsImpl<T> connections;
    private ServerSocketChannel sock;


    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
		this.connections = new ConnectionsImpl<>();

    }

    @Override
    public void serve() {

        try (ServerSocketChannel serverSock = ServerSocketChannel.open();
                Selector selector = Selector.open()) {
            serverSock.configureBlocking(false);
            serverSock.bind(new InetSocketAddress(port));
            serverSock.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Server started");
            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                for (SelectionKey key : selector.selectedKeys()){
                    if (key.isAcceptable()){
                        SocketChannel clientSock = serverSock.accept();
                        BidiMessagingProtocol<T> protocol = protocolFactory.get();
                        BlockingConnectionHandler<T> handler = new BlockingConnectionHandler<T>(
                                clientSock,
                                encdecFactory.get(),
                                protocol);
                        int conId = connectionCounter++;
                        connections.addConnection(conId,handler);
                        protocol.start( conId, connections);
                        execute(handler);
                    }
                }

            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<T>  handler);

}
