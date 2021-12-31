package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;



public class MessagingEncoderDecoderImpl implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1<<10];
    private int len = 0;
    private byte[] opcode = new byte[2];
    private int opcodeIndex = 0;


    @Override
    public String decodeNextByte(byte nextByte) {
        if (opcodeIndex <= 1){
            opcode[opcodeIndex] = nextByte;
            opcodeIndex++;
        }
        else if (nextByte == (byte)';'){
            return popString();
        }else if (nextByte == (byte)0){
            pushByte((byte)' ');

        }
        pushByte(nextByte);
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
        ArrayList<String> parsedMsg = new ArrayList<>(Arrays.asList(message.split(" ")));
        short opcode = getOpcode(parsedMsg.get(0));
        byte[] opcodeBytes = shortToBytes(opcode);
        pushByte(opcodeBytes[0]);
        pushByte(opcodeBytes[1]);
        for(int i =1; i< parsedMsg.size();i++){
            byte[] nextString = (parsedMsg.get(i)).getBytes();
            for (byte nextByte : nextString){
                pushByte(nextByte);
            }
            if (i<parsedMsg.size()-1){
                pushByte((byte)0);
            }
        }
        pushByte((byte)';');
        len = 0;
        return Arrays.copyOf(bytes,bytes.length);
    }

    private void pushByte(byte nextByte){
        if(len>= bytes.length){
            bytes = Arrays.copyOf(bytes,len*2);
        }
        bytes[len++] = nextByte;
    }

    private String popString(){
        String result = new String(bytes,2,len, StandardCharsets.UTF_8);
        String opcodeString = String.valueOf(opcode[1]);
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
}
