package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.srv.MessagingEncoderDecoderImpl;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.bidi.BidiMessagingProtocolImpl;

public class TPCMain {

    public static void main(String args[]){
        if (args.length == 1){
            int port = Integer.parseInt(args[0]);
            Server server = Server.threadPerClient(port, () -> new BidiMessagingProtocolImpl(), () -> new MessagingEncoderDecoderImpl());
            server.serve();
        }else{
            System.out.println("Not enough arguments");
        }
    }
}
