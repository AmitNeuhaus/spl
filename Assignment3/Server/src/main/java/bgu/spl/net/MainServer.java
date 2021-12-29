package bgu.spl.net;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

public class MainServer {

    public static void main(String args[]){

        Server server = Server.threadPerClient(5000, () -> new EchoProtocol(), ()-> new LineMessageEncoderDecoder());
        server.serve();
    }
}
