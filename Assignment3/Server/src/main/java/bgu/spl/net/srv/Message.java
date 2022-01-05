package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Message {
    public LinkedList<String> badWords = new LinkedList<>();
    public String content;

    public Message(String content){
        this.content = content;
    }
    public String asterisks(String word){
        StringBuilder outPut = new StringBuilder();
        for (int i = 0;i<word.length();i++){
            outPut.append("*");
        }
        return outPut.toString();
    }

    public String censorMsg(){
        ArrayList<String> unCensorMessage = new ArrayList<String>(Arrays.asList(this.content.split(" ")));
        for (int i = 0 ; i< unCensorMessage.size(); i++){
            String currentWord = unCensorMessage.get(i);
            String trimedWord = currentWord.replaceAll("[^a-zA-Z]","").toLowerCase();
            for (String badWord : badWords){
                if(trimedWord.contains(badWord)){
                    unCensorMessage.set(i,asterisks(currentWord));
                }
            }
        }
        return String.join(" ", unCensorMessage);
    }
}
