package bgu.spl.net.srv;

public class PM {
    String content;
    String timestamp;

    public PM(String content, String timestamp){
        this.content = filterPM(content);
        this.timestamp = timestamp;
    }

    public String getContent(){
        return content;
    }

    private String filterPM(String content){
        return "Filtering isnt implemented yet";
    }
}
