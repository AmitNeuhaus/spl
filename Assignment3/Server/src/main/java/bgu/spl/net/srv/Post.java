package bgu.spl.net.srv;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post implements Message {
        String content;

    public Post(String content){
        this.content = content;
    }

    public List<String> searchUsersInContent() {
        String regex = "\\B@\\w+";
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(regex)
                .matcher(content);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }

    public String getContent(){
        return content;
    }
}
