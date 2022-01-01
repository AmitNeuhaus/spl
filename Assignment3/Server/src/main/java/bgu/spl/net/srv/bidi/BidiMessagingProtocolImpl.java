package bgu.spl.net.srv.bidi;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {

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

        switch (opCode){

            case "1":
                String register_response = register(msg.get(1),msg.get(2),msg.get(3));
                connections.send(myConnectionId, register_response);
                break;
            case "2":
                String login_response = logIn(msg.get(1),msg.get(2));
                connections.send(myConnectionId, login_response);
            case "3":
                String logout_response = logOut();
                connections.send(myConnectionId, logout_response);
            case "4":

                String follow_response = follow(msg.get(1), msg.get(2));

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
        boolean didRegister = connections.register(myConnectionId,username,password,birthday);
        if(didRegister){
            return "ACK";
        }else{
            return "ERROR";
        }
    }

    public String logIn(String username,String password){
        boolean didLogIn = connections.logIn(myConnectionId,username,password);
        if(didLogIn){
            return "ACK";
        }else{
            return "ERROR";
        }
    }
    public String logOut(){
        boolean didLogOut = connections.logOut(myConnectionId);
        if(didLogOut){
            return "ACK";
        }else{
            return "ERROR";
        }
    }

    public String follow(boolean action, String userName){
        boolean didFollowAction;
        if (action)
            didFollowAction = connections.follow(userName);
        else
            didFollowAction = connections.unfollow(userName);

        if(didFollowAction){
            return "ACK";
        }else{
            return "ERROR";
        }
    }



//    HELPERS -------

    private String[] splitMessage(String msg){
        return msg.split(" ");
    }


}