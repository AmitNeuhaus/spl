package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements bgu.spl.net.api.bidi.Connections<T> {
    //Fields:

    ConcurrentHashMap<String, Integer> usernameToConId;
    ConcurrentHashMap<Integer, UserWrapper> conIdToUserWrapper;
    ConcurrentHashMap<String,UserInfo> users;


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
        conIdToUserWrapper.put(conId,new UserWrapper(handler));
    }

    public void register(int conId,String name,String password, String birthDay){
        UserInfo newUser = new UserInfo(name,password,birthDay);
        users.put(name,newUser);

    }

    public void logIn(int conId, String username, String password){
        UserInfo userInfo =  users.get(username);
        usernameToConId.put(username,conId);
        UserWrapper<T> wrapper = conIdToUserWrapper.get(conId);
        userInfo.setLoggedIn(true);
        wrapper.setUserName(userInfo.name);

    }

    public void logOut(int conId){
        UserWrapper wrapper = conIdToUserWrapper.get(conId);
        String userName = wrapper.getUserName();
        UserInfo user = users.get(userName);
        conIdToUserWrapper.remove(conId);
        usernameToConId.remove(user.name);
        user.setLoggedIn(false);


    }


    // GETTERS ---------------

    public String getUsername(int conId){
        return conIdToUserWrapper.get(conId).getUserName();
    }

    public Integer getConnectionId(String username){
        if (usernameToConId.containsKey(username)){
            return usernameToConId.get(username);
        }
        return null;
    }

    public UserInfo getUserInfo(String username){
        return users.get(username);
    }

    public UserInfo getUserInfo(int connId){
        return users.get(conIdToUserWrapper.get(connId).getUserName());
    }



    //get user information


    // Queries



    private boolean canSend(int conId){
        return true;
    }

    public boolean isUserNameExist(String userName){
        return users.containsKey(userName);
    }




}
