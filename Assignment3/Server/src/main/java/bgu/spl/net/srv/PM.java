package bgu.spl.net.srv;

import java.util.LinkedList;

public class PM extends Message {
    String timestamp;

    public PM(String content, String timestamp, LinkedList<String> badWords){
        super(content,badWords);
        this.content = censorMsg();
        this.timestamp = timestamp;
    }

    public String getContent(){
        return content;
    }
}

