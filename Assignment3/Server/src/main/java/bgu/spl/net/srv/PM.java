package bgu.spl.net.srv;

public class PM extends Message {
    String timestamp;

    public PM(String content, String timestamp){
        super(content);
        this.content = censorMsg();
        this.timestamp = timestamp;
    }

    public String getContent(){
        return content;
    }
}

