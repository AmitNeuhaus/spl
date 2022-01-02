package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private final ConcurrentHashMap<String, UserInfo> users;
    private final ConcurrentHashMap<Integer, String> activeUsers;

    //Constructor:
    public DataBase(){
        users = new ConcurrentHashMap<>();
        activeUsers = new ConcurrentHashMap<>();
    }

    public void register(String name,String password, String birthDay){
        UserInfo newUser = new UserInfo(name,password,birthDay);
        users.put(name,newUser);
    }

    public void logIn(int conId, String username){
        UserInfo user = users.get(username);
        user.setConID(conId);
        user.setLoggedIn(true);
        activeUsers.put(conId,username);
    }

    public void logOut(int connectionId) {
        String username = activeUsers.get(connectionId);
        activeUsers.remove(connectionId);
        UserInfo user = users.get(username);
        user.setLoggedIn(false);
        user.setConID(-1);
    }


    //GETTERS ---------

    public String getUsername(int conId){
        return activeUsers.get(conId);
    }

    public Integer getConnectionId(String username){
        return users.get(username).getCurrentConId();
    }

    public UserInfo getUserInfo(String username){
        return users.get(username);
    }

    public UserInfo getUserInfo(int connId){
        return users.get(activeUsers.get(connId));
    }

    //QUERIES-------

    public boolean isRegister(String username){
        return users.containsKey(username);
    }

    public boolean isLoggedIn(String username){
        if (isRegister(username)){
            return users.get(username).isLoggedIn();
        }
        return false;
    }


}
