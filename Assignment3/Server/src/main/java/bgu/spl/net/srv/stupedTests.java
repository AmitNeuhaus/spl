package bgu.spl.net.srv;

import java.lang.reflect.Array;
import java.util.*;

public class stupedTests {
    public static void main(String[] args) {
        MessagingEncoderDecoderImpl encdec = new MessagingEncoderDecoderImpl();
        Scanner scanner = new Scanner(System.in);
        String input = "REGISTER tom 1235 20-10-1995";
        System.out.println("your input is: "+input );
        byte[] encoded = encdec.encode(input);
        System.out.println("encoded: "+ Collections.singletonList(encoded));
        String decoded = encdec.decodeMessage(encoded);
        System.out.println(decoded);
        int i =0;
//        for (String s : decoded){
//            System.out.println(i+" "+s);
//            i++;
//        }

    }
}
