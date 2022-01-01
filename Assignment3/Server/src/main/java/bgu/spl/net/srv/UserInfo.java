package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserInfo {
    String name;
    String password;
    String birthDay;
    boolean loggedIn;
    ConcurrentLinkedQueue<Integer> followers;



    public UserInfo(){
        this.name = "UnregisteredClient";
        this.password = "UnregisteredClient";
        this.birthDay =  "UnregisteredClient";
        this.loggedIn = false;
    }

    public void setInfo(String name, String password, String birthDay){
        this.name = name;
        this.password = password;
        this.birthDay = birthDay;
    }
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public ConcurrentLinkedQueue<Integer> getFollowers(){
        return  followers;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }


    //SETTERS ---------

    public void setLoggedIn(boolean t){
        loggedIn = t;
    }



}
