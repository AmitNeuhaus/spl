package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MassagingEncoderDecoderImpl implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1<<10];
    private int len = 0;


    @Override
    public String decodeNextByte(byte nextByte) {
        if (nextByte == (byte)';'){
            return popString();
        }
        pushByte(nextByte);
        return null;
    }

    @Override
    public byte[] encode(String message) {
        return new byte[0];
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
}