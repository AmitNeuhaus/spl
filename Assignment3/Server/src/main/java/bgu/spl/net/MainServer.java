package bgu.spl.net;


import bgu.spl.net.srv.MassagingEncoderDecoderImpl;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.bidi.BidiMessagingProtocolImpl;

public class MainServer {

    public static void main(String args[]){

        Server server = Server.threadPerClient(5000, () -> new BidiMessagingProtocolImpl<>(), ()-> new MassagingEncoderDecoderImpl());
        server.serve();
    }
}
