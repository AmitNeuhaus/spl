package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.MessagingEncoderDecoderImpl;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.bidi.BidiMessagingProtocolImpl;

public class ReactorMain {

    public static void main(String args[]){
        if (args.length == 2){
            int port = Integer.parseInt(args[0]);
            int numOfThreads = Integer.parseInt(args[1]);
            Server server = Server.reactor(numOfThreads, port, () -> new BidiMessagingProtocolImpl(), () -> new MessagingEncoderDecoderImpl());
            server.serve();
        }else{
            System.out.println("Not enough arguments");
        }

    }
}
