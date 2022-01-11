package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements bgu.spl.net.api.bidi.Connections<T> {
    //Fields:
    ConcurrentHashMap<Integer, ConnectionHandler<T>> conIdToUserHandler;

    public ConnectionsImpl(){
        conIdToUserHandler = new ConcurrentHashMap<>();
    }


    // ACTIONS --------------
    @Override
    public boolean send(int connectionId, T msg) {
        if (canSend(connectionId)){
            conIdToUserHandler.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        conIdToUserHandler.forEach((conId,handler) -> {
            handler.send(msg);
        });
    }

    @Override
    public void disconnect(int connectionId) {
        conIdToUserHandler.remove(connectionId);
    }

    public void addConnection(int conId, ConnectionHandler<T> handler){
        conIdToUserHandler.put(conId,handler);
    }

    private boolean canSend(int conId){
        return true;
    }














}
