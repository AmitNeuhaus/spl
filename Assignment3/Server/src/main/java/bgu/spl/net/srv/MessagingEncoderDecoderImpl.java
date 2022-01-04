package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.jar.JarOutputStream;


public class MessagingEncoderDecoderImpl implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1<<10];
    private int len = 0;
    private final byte[] opcode = new byte[2];
    private int opcodeIndex = 0;
    private final HashMap<String,String> badWords = new HashMap<>();


    @Override
    public String decodeNextByte(byte nextByte) {
        if (opcodeIndex <= 1){
            opcode[opcodeIndex] = nextByte;
            opcodeIndex++;
        }
        else if (nextByte == (byte)';'){
            return popString();
        }else if (nextByte == (byte)0){
            nextByte = ((byte)' ');
            pushByte(nextByte);
        }else{
            pushByte(nextByte);
        }
        return null;
    }

    public String decodeMessage(byte[] msg){
        String output = null;
        int i = 0;
        while(output == null){
            output = decodeNextByte(msg[i]);
            i++;
        }
        return output;
    }

    public byte[] getFullMessage(byte nextByte){
        if (nextByte == (byte)';'){
            pushByte(nextByte);
            len = 0;
            return Arrays.copyOf(bytes,bytes.length);
        }
        pushByte(nextByte);
        return null;
    }



    @Override
    public byte[] encode(String message) {
        ArrayList<String> parsedMsg = censorMsg(message);
        short opcodeShort = getOpcode(parsedMsg.get(0));
        byte[] opcodeBytes = shortToBytes(opcodeShort);
        for (byte b : opcodeBytes){
            pushByte(b);
        }
        for(int i =1; i< parsedMsg.size();i++){new ArrayList<>(Arrays.asList(message.split(" ")));
            byte[] nextString = (parsedMsg.get(i)).getBytes();
            for (byte nextByte : nextString){
                pushByte(nextByte);
            }
            if (i<parsedMsg.size()-1){
                pushByte((byte)0);
            }
        }
        pushByte((byte)';');
        System.out.println(len);
        len = 0;
        System.out.println(parsedMsg);
        return Arrays.copyOf(bytes,bytes.length);
    }

    private void pushByte(byte nextByte){
        if(len>= bytes.length){
            bytes = Arrays.copyOf(bytes,len*2);
        }
        bytes[len++] = nextByte;
    }

    private String popString(){
        String result = new String(bytes,0,len, StandardCharsets.UTF_8);
        String opcodeString = String.valueOf(bytesToShort(opcode));
        System.out.println("opcode is: "+ opcodeString + " using their method");
        len = 0;
        opcodeIndex =0;
        return opcodeString + " " + result;
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

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }


    private ArrayList<String> censorMsg(String msg){
        ArrayList<String> unCensorMessage = new ArrayList<String>(Arrays.asList(msg.split(" ")));
        for (int i = 0 ; i< unCensorMessage.size(); i++){
            String currentWord = unCensorMessage.get(i);
            String trimedWord = currentWord.replaceAll("[^a-zA-Z]","").toLowerCase();
            for (String badWord : badWords.keySet()){
                if(trimedWord.contains(badWord)){
                    unCensorMessage.set(i,asterisks(currentWord));
                }
            }
        }
        return unCensorMessage;
    }

    private String asterisks(String word){
        StringBuilder outPut = new StringBuilder();
        for (int i = 0;i<word.length();i++){
            outPut.append("*");
        }
        return outPut.toString();
    }





}
