package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;


import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.jar.JarOutputStream;


public class MessagingEncoderDecoderImpl implements MessageEncoderDecoder<ArrayList<String>> {

    private byte[] bytes = new byte[1<<10];
    private int len = 0;
    private final byte[] opcode = new byte[2];
    private int opcodeIndex = 0;
    private final ArrayList<String> parsedMessage = new ArrayList<>();

    @Override
    public ArrayList<String> decodeNextByte(byte nextByte) {
        if (opcodeIndex <= 1){
            opcode[opcodeIndex] = nextByte;
            if (opcodeIndex == 1){
                short opcodeShort = bytesToShort(opcode);
                parsedMessage.add(String.valueOf(opcodeShort));
            }
            opcodeIndex++;
        }
        else if (parsedMessage.get(0).equals("4") && parsedMessage.size() == 1){
            pushByte(nextByte);
            parsedMessage.add(popString());
        }
        else if (nextByte == (byte)';'){
            ArrayList<String> result = new ArrayList<>(parsedMessage);
            parsedMessage.clear();
            opcodeIndex =0;
            return result;
        }else if (nextByte == (byte)'\0'){
            parsedMessage.add(popString());
        }else{
            pushByte(nextByte);
        }
        return null;
    }

//    public String decodeMessage(byte[] msg){
//        String output = null;
//        int i = 0;
//        while(output == null){
//            output = decodeNextByte(msg[i]);
//            i++;
//        }
//        return output;
//    }
//
//    public byte[] getFullMessage(byte nextByte){
//        if (nextByte == (byte)';'){
//            pushByte(nextByte);
//            len = 0;
//            return Arrays.copyOf(bytes,bytes.length);
//        }
//        pushByte(nextByte);
//        return null;
//    }



    @Override
    public byte[] encode(ArrayList<String> parsedMsg) {
        short opcode = getOpcode(parsedMsg.get(0));
        byte[] opcodeBytes = shortToBytes(opcode);
        pushByte(opcodeBytes[0]);
        pushByte(opcodeBytes[1]);
        switch (opcode){

            //ERROR
            case 11:
                encodeError(parsedMsg);
                break;
            //Notification
            case 9:
                encodeNotification(parsedMsg);
                break;
            //ACK
            case 10:
                String ackType = parsedMsg.get(1);
                //Follow ACK
                if(ackType.equals("4"))
                    encodeFollowAck(parsedMsg);
                //STAT & LOGSTAT ACK
                else if (ackType.equals("7") || ackType.equals("8"))
                    encodeStatAck(parsedMsg);
                // REGULAR ACK
                else
                    encodeRegularAck(parsedMsg);
                break;

        }
        // Encode end of msg
        pushByte((byte)';');
        //"Reset Byte Array"
        byte[] res = Arrays.copyOf(bytes,len);
        len = 0;
        return res;
    }

    private void pushByte(byte nextByte){
        if(len>= bytes.length){
            bytes = Arrays.copyOf(bytes,len*2);
        }
        bytes[len++] = nextByte;
    }

    private String popString(){
        String result = new String(bytes,0,len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private short getOpcode(String input){
        switch (input){
            case"REGISTER":
                return 1;
            case "LOGIN":
                return 2;
            case "LOGOUT":
                return 3;
            case "FOLLOW":
                return 4;
            case "POST":
                return 5;
            case "PM":
                return 6;
            case "LOGSTAT":
                return 7;
            case "STAT":
                return 8;
            case "NOTIFICATION":
                return 9;
            case "ACK":
                return 10;
            case "ERROR":
                return 11;
            case "BLOCK":
                return 12;
            default:
                return 13;
        }

    }
    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private byte[] stringToShort(String s){
        short command = Short.parseShort(s);
        return shortToBytes(command);
    }

    private void sendBytes(byte[] bytes ){
        for (byte b: bytes){
            pushByte(b);
        }
    }
    private void encodeString(String str){
        byte[] stringBytes = str.getBytes();
        sendBytes(stringBytes);
        pushByte((byte)'\0');
    }

    private void encodeShort(String str){
        byte[] shortBytes = stringToShort(str);
        sendBytes(shortBytes);
    }


    // Encoding msg types;

    private void encodeRegularAck(ArrayList<String>parsedMsg){
        String stringAckCommand = parsedMsg.get(1);
        encodeShort(stringAckCommand);
    }

    private void encodeFollowAck(ArrayList<String> parsedMsg){
        encodeRegularAck(parsedMsg);
        String userName = parsedMsg.get(2);
        encodeString(userName);
    }

    private void encodeStatAck(ArrayList<String> parsedMsg){
        encodeRegularAck(parsedMsg);
        String age = parsedMsg.get(2);
        String numPosts = parsedMsg.get(3);
        String numFollowers = parsedMsg.get(4);
        String numFollowing = parsedMsg.get(4);
        encodeShort(age);
        encodeShort(numPosts);
        encodeShort(numFollowers);
        encodeShort(numFollowing);
    }

    private void encodeError(ArrayList<String> parsedMsg){
        encodeRegularAck(parsedMsg);
    }

    private void encodeNotification(ArrayList<String> parsedMsg){
        String notificationType = parsedMsg.get(1);
        String postingUser = parsedMsg.get(2);
        String content = parsedMsg.get(3);
        pushByte(Integer.valueOf(notificationType).byteValue());
        encodeString(postingUser);
        encodeString(content);

    }

}
