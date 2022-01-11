package bgu.spl.net;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.MessagingEncoderDecoderImpl;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.bidi.BidiMessagingProtocolImpl;

public class MainServer {

    public static void main(String args[]){
        Reactor server = new Reactor(5,1234, () -> new BidiMessagingProtocolImpl(), ()-> new MessagingEncoderDecoderImpl());
        server.serve();
    }
}
