package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;


public class UserWrapper<T> {
    private ConnectionHandler<T> handler;
    private String username;

    public UserWrapper(ConnectionHandler<T> handler){
        this.handler = handler;
        this.username = null;
    }

    public ConnectionHandler<T> getHandler(){
        return handler;
    }

    public String getUserName(){
        return username;
    }

    public void setUserName(String userName){
        this.username = userName;
    }
}
