package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements bgu.spl.net.api.bidi.Connections<T> {
    //Fields:

    ConcurrentHashMap<String, Integer> usernameToConId;
    ConcurrentHashMap<Integer, UserWrapper<T>> conIdToUserWrapper;


    public ConnectionsImpl(){
        usernameToConId = new ConcurrentHashMap<>();
        conIdToUserWrapper = new ConcurrentHashMap<>();
    }


    // ACTIONS --------------
    @Override
    public boolean send(int connectionId, T msg) {
        if (canSend(connectionId)){
            conIdToUserWrapper.get(connectionId).getHandler().send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        conIdToUserWrapper.forEach((conId,wrapper) -> {
            ConnectionHandler<T> handler = wrapper.getHandler();
            handler.send(msg);

        });
    }

    @Override
    public void disconnect(int connectionId) {
        usernameToConId.remove(getUsername(connectionId));
        conIdToUserWrapper.remove(connectionId);

    }

    public void addConnection(int conId, ConnectionHandler<T> handler){
        UserWrapper<T> userWrapper = new UserWrapper<>(handler, new UserInfo());
        conIdToUserWrapper.put(conId,userWrapper);
    }

    public boolean register(int conId,String name,String password, String birthDay){
        if (canRegisterNewUser(name)){
            conIdToUserWrapper.get(conId).getUserInfo().setInfo(name,password,birthDay);
            usernameToConId.put(name,conId);
            return true;
        }
        return false;
    }

    public boolean logIn(int conId, String username, String password){
        if (canLogIn(conId, username, password)){
            conIdToUserWrapper.get(conId).getUserInfo().setLoggedIn(true);
            return true;
        }
        return false;
    }

    public boolean logOut(int conId){
        if (canLogOut(conId)){
            conIdToUserWrapper.get(conId).getUserInfo().setLoggedIn(false);
            return true;
        }
        return false;
    }

    public boolean follow(String userName){
        if (canFollow(userName)){
            conIdToUserWrapper.get(conId).getUserInfo().setLoggedIn(false);
            return true;
        }
        return false;
    }

    public boolean follow(String userName){
        if (canUnfollow(userName)){
            conIdToUserWrapper.get(conId).getUserInfo().setLoggedIn(false);
            return true;
        }
        return false;
    }


    // GETTERS ---------------

    private String getUsername(int conId){
        return conIdToUserWrapper.get(conId).getUserInfo().getName();
    }

    public Integer getConnectionId(String username){
        if (usernameToConId.containsKey(username)){
            return usernameToConId.get(username);
        }
        return null;
    }

}
