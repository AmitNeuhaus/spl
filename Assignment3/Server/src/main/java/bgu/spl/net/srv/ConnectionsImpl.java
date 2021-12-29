package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements bgu.spl.net.api.bidi.Connections<T> {
    //Fields:

    ConcurrentHashMap<String, Integer> usernameToConId;
    ConcurrentHashMap<Integer, UserWrapper> conIdToUserWrapper;

    @Override
    public boolean send(int connectionId, T msg) {
        try{
            if (canSend(connectionId)){
                conIdToUserWrapper.get(connectionId).getHandler().send(msg);
                return true;
            }
            return false;

        }catch (IOException e){
            System.out.println("Couldnt send msg to: " + conIdToUserWrapper.get(connectionId).getUserInfo().getName());
            return false;

        }

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

    public boolean register(int conId, ConnectionHandler<T> handler, String username, String password, String birthDay){
        if(canRegisterNewUser(username)){
            UserWrapper<T> userWrapper = new UserWrapper<>(handler, new UserInfo(username,password,birthDay));
            usernameToConId.put(username,conId);
            conIdToUserWrapper.put(conId,userWrapper);
            return true;
        }
        return false;

    }

    private boolean canRegisterNewUser(String username){
        return usernameToConId.containsKey(username);
    }

    private String getUsername(int conId){
        return conIdToUserWrapper.get(conId).getUserInfo().getName();
    }

    private boolean canSend(int conId){
        return true;
    }

    public Integer getConnectionId(String username){
        if (usernameToConId.containsKey(username)){
            return usernameToConId.get(username);
        }
        return null;
    }
}
