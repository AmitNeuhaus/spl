package bgu.spl.net.impl.echo;

import bgu.spl.net.srv.MassagingEncoderDecoderImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws IOException {

//        if (args.length == 0) {
//            args = new String[]{"localhost", "hello"};
//        }
//
//        if (args.length < 2) {
//            System.out.println("you must supply two arguments: host, message");
//            System.exit(1);
//        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (
                Socket sock = new Socket("127.0.0.1", 5000);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            MassagingEncoderDecoderImpl  encDec = new MassagingEncoderDecoderImpl();
            System.out.println("sending message to server");
            out.write("REGISTER tom 1235 20-10-1995;");
            out.flush();

            System.out.println("awaiting response");
            Thread.sleep(2000);
            System.out.println("awake");
            byte[] msg = null;
            int read =-1;
            while(msg==null){
                if ((read=in.read())>=0){
                    msg = encDec.getFullMessage((byte)read);
                }

            }
            String decoded = encDec.decodeMessage(msg);
            System.out.println("message from server: "+ decoded);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
