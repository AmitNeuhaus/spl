package bgu.spl.net.srv;

public class UserInfo {
    String name;
    String password;
    String birthDay;


    public UserInfo(){
        this.name = "UnregisteredClient";
        this.password = "UnregisteredClient";
        this.birthDay =  "UnregisteredClient";
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



}
