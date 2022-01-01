package bgu.spl.net.srv;

import java.util.concurrent.CopyOnWriteArrayList;

public class UserInfo {
    String name;
    String password;
    String birthDay;
    boolean loggedIn;
    CopyOnWriteArrayList<Integer> followers = new CopyOnWriteArrayList<>();



    public UserInfo(){
        this.name = "";
        this.password = "";
        this.birthDay =  "";
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

    public CopyOnWriteArrayList<Integer> getFollowers(){
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
