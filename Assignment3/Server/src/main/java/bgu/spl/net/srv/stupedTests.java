package bgu.spl.net.srv;

import java.util.*;

public class stupedTests {
    public static void main(String[] args) {
        MessagingEncoderDecoderImpl encdec = new MessagingEncoderDecoderImpl();
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> input1 = new ArrayList<>(Arrays.asList(("REGISTER tom 1235 20-10-1995").split(" ")));

        System.out.println("your input is: "+ input1 );
        byte[] encoded1 = encdec.encode(input1);
        ArrayList<String> decoded1 = null;
        int i =0;
        while(decoded1 == null){
            decoded1 = encdec.decodeNextByte(encoded1[i]);
            i++;
        }
        System.out.println(decoded1);

        ArrayList<String> input2= new ArrayList<>(Arrays.asList(("LOGIN tom 1235 1").split(" ")));
        System.out.println("your input is: "+ input2 );
        byte[] encoded2 = encdec.encode(input2);
        ArrayList<String> decoded2 = null;
        i =0;
        while(decoded2 == null){
            decoded2 = encdec.decodeNextByte(encoded2[i]);
            i++;
        }
        System.out.println(decoded2);
        ArrayList<String> input3 = new ArrayList<>(Arrays.asList(("LOGOUT").split(" ")));
        System.out.println("your input is: "+ input3 );
        byte[] encoded3 = encdec.encode(input3);
        ArrayList<String> decoded3 = null;
        i =0;
        while(decoded3 == null){
            decoded3 = encdec.decodeNextByte(encoded3[i]);
            i++;
        }
        System.out.println(decoded3);
        ArrayList<String> input4 = new ArrayList<>(Arrays.asList(("FOLLOW 1 TOM").split(" ")));
        System.out.println("your input is: "+ input4 );
        byte[] encoded4 = encdec.encode(input4);
        ArrayList<String> decoded4 = null;
        i =0;
        while(decoded4 == null){
            decoded4 = encdec.decodeNextByte(encoded4[i]);
            i++;
        }
        System.out.println(decoded4);
        ArrayList<String> input5 = new ArrayList<>(Arrays.asList(("POST jasdfljadsflkajdslkfjaklsdfjalskdfj").split(" ")));
        System.out.println("your input is: "+ input5 );
        byte[] encoded5 = encdec.encode(input5);
        ArrayList<String> decoded5 = null;
        i =0;
        while(decoded5 == null){
            decoded5 = encdec.decodeNextByte(encoded5[i]);
            i++;
        }
        System.out.println(decoded5);
        ArrayList<String> input6 = new ArrayList<>(Arrays.asList(("PM tom klsdfjgklsjdfghksjldfhgkldfj").split(" ")));
        System.out.println("your input is: "+ input6 );
        byte[] encoded6 = encdec.encode(input6);
        ArrayList<String> decoded6 = null;
        i =0;
        while(decoded6 == null){
            decoded6 = encdec.decodeNextByte(encoded6[i]);
            i++;
        }
        System.out.println(decoded6);

    }
}
