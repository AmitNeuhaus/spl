package bgu.spl.net.srv.bidi;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.UserInfo;



import java.util.ArrayList;
import java.util.Collections;


public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {

    private DataBase dataBase;
    private ConnectionsImpl<String> connections;
    private int myConnectionId;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connections = (ConnectionsImpl<String>) connections;
        this.myConnectionId = connectionId;
        dataBase =  DataBase.getInstance();
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
                String follow_response = logOut();

            case "5":
                System.out.println("NOT IMPLEMENTED YET 5");
            case "6":
                System.out.println("NOT IMPLEMENTED YET 6");
            case "7":
                System.out.println("NOT IMPLEMENTED YET 7");
            case "8":
                System.out.println("NOT IMPLEMENTED YET 8");
            case "9":
                System.out.println("NOT IMPLEMENTED YET 9");
            case "10":
                System.out.println("NOT IMPLEMENTED YET 10");
            case "11":
                System.out.println("NOT IMPLEMENTED YET 11");
            case "12":
                System.out.println("NOT IMPLEMENTED YET 12");

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

//    HELPERS -------

    private String[] splitMessage(String msg){
        return msg.split(" ");
    }


}