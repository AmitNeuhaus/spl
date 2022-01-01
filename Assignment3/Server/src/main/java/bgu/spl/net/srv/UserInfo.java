package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserInfo {
    String name;
    String password;
    String birthDay;
    boolean loggedIn;
    ConcurrentLinkedQueue<String> followers;
    ConcurrentLinkedQueue<String> blocked;



    public UserInfo(String name, String password, String birthDay){
        this.name = name;
        this.password = password;
        this.birthDay =  birthDay;
        this.loggedIn = false;
        followers = new ConcurrentLinkedQueue<>();
        blocked = new ConcurrentLinkedQueue<>();
    }

    public void setInfo(String name, String password, String birthDay){
        this.name = name;
        this.password = password;
        this.birthDay = birthDay;
    }

    //GETTERS -------------

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public ConcurrentLinkedQueue<String> getFollowers(){
        return  followers;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public int getNumberOfFollowers(){
        return followers.size();
    }
    public int getNumberOfBlocked(){
        return blocked.size();
    }


    //SETTERS ---------

    public void setLoggedIn(boolean t){
        loggedIn = t;
    }

    public void addFollower(String name) {
        followers.add(name);
    }

    public void removeFollower(String name){
        followers.remove(name);
    }

    public void addBlocked(String name){
        blocked.add(name);
        removeFollower(name);
    }

    public void removeBlocked(String name){
        blocked.remove(name);
    }

    //QUERIES -------

    public boolean isFollowing(String name){
        return followers.contains(name);
    }

    public boolean isBlocked(String name){
        return blocked.contains(name);
    }






}
