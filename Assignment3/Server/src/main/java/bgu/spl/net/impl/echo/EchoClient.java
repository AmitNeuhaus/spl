package bgu.spl.net.impl.echo;


import bgu.spl.net.srv.MessagingEncoderDecoderImpl;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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
                BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream())) {

            MessagingEncoderDecoderImpl encDec = new MessagingEncoderDecoderImpl();
            System.out.println("sending message to server");
            byte[] encoded = encDec.encode(new ArrayList<>(Arrays.asList("POST ani @tom ve @dvir went to letayel ehad nafal ve @maor hitpozzez oi looooo @avram ma omerrrr")));
            out.write(encoded,0,encoded.length);
            out.flush();

            System.out.println("awaiting response");
            Thread.sleep(2000);
            System.out.println("awake");
            ArrayList<String> msg = null;
            int read =-1;
            while(msg==null){
                if ((read=in.read())>=0){
                    msg = encDec.decodeNextByte((byte)read);
                }

            }

            System.out.println("message from server: "+ msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
