package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;


public class UserWrapper<T> {
    private ConnectionHandler<T> handler;
    private final UserInfo userInfo;

    public UserWrapper(ConnectionHandler handler, UserInfo userInfo){
        this.handler = handler;
        this.userInfo = userInfo;
    }

    public ConnectionHandler getHandler(){
        return handler;
    }

    public UserInfo getUserInfo(){
        return userInfo;
    }
}
