package bgu.spl.net.srv.bidi;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {

    private DataBase dataBase;
    private ConnectionsImpl<String> connections;
    private int myConnectionId;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections = (ConnectionsImpl<String>) connections;
        this.myConnectionId = connectionId;
    }

    @Override
    public void process(String message) {
        ArrayList<String> msg = new ArrayList<>();
        Collections.addAll(msg, splitMessage(message));
        String opCode = msg.get(0);

        String response;
        switch (opCode){
            case "1":
                response = register(msg.get(1),msg.get(2),msg.get(3));
                connections.send(myConnectionId, response);
                break;
            case "2":
                response = logIn(msg.get(1),msg.get(2));
                connections.send(myConnectionId, response);
            case "3":
                response = logOut();
                connections.send(myConnectionId, response);
            case "4":
                boolean followOrUnfollow = msg.get(1)=="0"? true: false;
                response = follow(followOrUnfollow, msg.get(2));
                connections.send(myConnectionId, response);
            case "5":
                String[] parsedMessage= message.split(" ", 2);
                response = post(parsedMessage[1]);
                connections.send(myConnectionId, response);
            case "6":
                String pmContent= "NOT implemnted parsing yet";
                String followerName ="NOT implemnted parsing yet";
                String timestamp= "NOT implemnted parsing yet";
                pm(followerName, pmContent, timestamp);
            case "7":
                logStat();

            case "8":
                String usersList ="UsersList not provided yet";
                stat(usersList);
            case "9":
                System.out.println("NOT IMPLEMENTED YET 9");
            case "10":
                System.out.println("NOT IMPLEMENTED YET 10");
            case "11":
                System.out.println("NOT IMPLEMENTED YET 11");
            case "12":
                String userNameToBlock="Not implmented blokcing user parsing yet";
                response = block(userNameToBlock);
                connections.send(myConnectionId, response);
            default:
                System.out.println("Couldn't recognize op code");


        }



        connections.send(myConnectionId, message);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }


//    ACTIONS-------
    public String register(String username,String password, String birthday){
        if (canRegisterNewUser(username)) {
            dataBase.register(username, password, birthday);
            return "ACK";
        }
        return "ERROR";
    }

    public String logIn(String username,String password){
        if(canLogIn(username,password)) {
            dataBase.logIn(myConnectionId, username);
            return "ACK";
        }
        return "ERROR";

    }
    public String logOut(){
        if(canLogOut(myConnectionId)){
            dataBase.logOut(myConnectionId);
            connections.disconnect(myConnectionId);
        }
        return "ERROR";
    }

    public String follow(boolean action, String userName){
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        if (action) {
            if(canFollow(userName)){
                user.follow(userName);
                return "ACK";
            }
            return "ERROR";
        }else{
            if (canUnfollow(userName)) {
                user.unFollow(userName);
                return "ACK";
            }
            return "ERROR";
        }
    }

    public String post(String content){
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        if(canPost(user)) {
            Post post = new Post(content);
            user.addPost(post);
            List<String> matchingUsers = post.searchUsersInContent();
            // Add Post to tagged users
            for (String usertag : matchingUsers) {
                String username = usertag.substring(1);
                UserInfo taggedUser = dataBase.getUserInfo(username);
                taggedUser.addToReadingList(post);
            }

            //Add Post to followers
            ConcurrentLinkedQueue<String> followers = user.getFollowers();
            for (String follower : followers) {
                UserInfo taggedUser = dataBase.getUserInfo(follower);
                taggedUser.addToReadingList(post);
            }
            return "ACK";
        }
        return "ERROR";

    }


    public String pm(String followerName, String content, String timestamp){
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        UserInfo follower = dataBase.getUserInfo(followerName);

        if(canPm(user, follower)) {
            PM pm = new PM(content, timestamp);
            user.addPm(pm);
            follower.addToReadingList((Message) pm);
            return "ACK";
        }
        return "ERROR";
    }


    public String logStat(){
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        if(user != null && user.isLoggedIn()) {
            ConcurrentHashMap<Integer, String> activeUsers = dataBase.getActiveUsers();
            for(Integer i: activeUsers.keySet()) {
                String userName = activeUsers.getOrDefault(i, null);
                if (userName != null){
                    UserInfo activeUserInfo = dataBase.getUserInfo(userName);
                    connections.send(myConnectionId,"ACK " + activeUserInfo.getStat());
                }
            }
        }
        return "ERROR";
    }


    public String stat(String usersList){
        String[] interestedUserNames = usersList.split("\\|");
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        if(user != null && user.isLoggedIn()) {
            for(String userName: interestedUserNames) {
                if (userName != null){
                    UserInfo activeUserInfo = dataBase.getUserInfo(userName);
                     connections.send(myConnectionId,"ACK " + activeUserInfo.getStat());
                }
            }
        }
        return "ERROR";
    }



    public String block(String username){
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        UserInfo blockUser = dataBase.getUserInfo(username);
        if (blockUser != null) {
            user.unFollow(blockUser.getName());
            blockUser.unFollow(user.getName());
            return "ACK";
        }
        return "Error";
    }

    // Queries

    private boolean canRegisterNewUser(String userName){
        UserInfo user = dataBase.getUserInfo(userName);
        return !(user == null);
    }

    private boolean canSend(int conId){
        return true;
    }

    private boolean canLogIn(String username, String password) {
        UserInfo user = dataBase.getUserInfo(username);
        boolean userExists = user != null;
        if (userExists){
            boolean matchingPassowrd = user.getPassword().equals(password);
            return (userExists && matchingPassowrd && !user.isLoggedIn());
        }
        return false;
    }

    private boolean canLogOut(int connectionId) {
        UserInfo user = dataBase.getUserInfo(connectionId);
        boolean userExists = user != null;
        return (userExists && user.isLoggedIn());
    }

    private boolean canFollow(String userName) {
        UserInfo user = dataBase.getUserInfo(myConnectionId);
        UserInfo follower = dataBase.getUserInfo(userName);
        boolean followerExists = follower != null;
        return (followerExists && user.isLoggedIn() && !user.isFollower(userName));
    }

    private boolean canUnfollow(String userName) {
        return true;
    }

    public boolean canPost(UserInfo user){
        return user.isLoggedIn();
    }

    public boolean canPm(UserInfo user, UserInfo follower){
        return user.isLoggedIn() && follower != null &&user.isFollower(follower.getName());
    }

//    HELPERS -------

    private String[] splitMessage(String msg){
        return msg.split(" ");
    }


}