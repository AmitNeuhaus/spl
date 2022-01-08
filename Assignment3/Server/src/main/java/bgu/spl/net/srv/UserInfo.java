package bgu.spl.net.srv;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserInfo {
    String name;
    String password;
    LocalDate birthDay;
    boolean loggedIn;
    int currentConId;
    int posts;
    ConcurrentLinkedQueue<String> followers;
    ConcurrentLinkedQueue<String> following;
    ConcurrentLinkedQueue<String> blocked;
    ConcurrentLinkedQueue<Post> myPosts;
    ConcurrentLinkedQueue<PM> myPms;
    ConcurrentLinkedQueue<ArrayList<String>> UnreadMessages;


    public UserInfo(String name, String password, String birthDay){
        this.name = name;
        this.password = password;
        convertBirthday(birthDay);
        this.loggedIn = false;
        this.posts = 0;
        followers = new ConcurrentLinkedQueue<>();
        blocked = new ConcurrentLinkedQueue<>();
        following = new ConcurrentLinkedQueue<>();
        UnreadMessages = new ConcurrentLinkedQueue<>();
        myPosts = new ConcurrentLinkedQueue<>();
        myPms = new ConcurrentLinkedQueue<>();
    }

    public void setInfo(String name, String password, String birthDay){
        this.name = name;
        this.password = password;
        convertBirthday(birthDay);
    }

    private void convertBirthday(String birthday){
        ArrayList<String> birthdayList = new ArrayList(Arrays.asList(birthday.split("-")));
        int year = Integer.parseInt(birthdayList.get(2));
        int month = Integer.parseInt(birthdayList.get(1));
        int day = Integer.parseInt(birthdayList.get(0));
        birthDay = LocalDate.of(year,month,day);
    }

    //GETTERS -------------

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return Period.between(birthDay,LocalDate.now()).getYears();
    }

    public ConcurrentLinkedQueue<String> getFollowers(){
        return  followers;
    }

    public ConcurrentLinkedQueue<Post> getPosts(){
        return  myPosts;
    }
    public ConcurrentLinkedQueue<PM> getPms(){
        return myPms;
    }


    public ConcurrentLinkedQueue<String> getFollowing(){return following;}

    public ConcurrentLinkedQueue<String> getBlocked(){return blocked;}

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public int getCurrentConId(){
        return currentConId;
    }

    public int getNumberOfFollowers(){
        return followers.size();
    }
    public int getNumberOfBlocked(){
        return blocked.size();
    }
    public int getNumberOfFollowing(){return following.size();}
    public int getNumberOfPosts(){return posts;}

    public ArrayList<String> getStat(){
        ArrayList<String> userStat = new ArrayList<>();
        userStat.add(getAge().toString());
        userStat.add(String.valueOf(getPosts().size()));
        userStat.add(String.valueOf(getFollowers().size()));
        userStat.add(String.valueOf(getFollowing().size()));
        return  userStat;
    }

    public ConcurrentLinkedQueue<ArrayList<String>> getUnreadMessages(){
        return UnreadMessages;
    }
    //SETTERS ---------

    public synchronized void  setLoggedIn(boolean t){
        loggedIn = t;
    }

    public void post(){posts++;}

    public void addFollower(String name) {
        followers.add(name);
    }

    public void setConID(int conId){
        currentConId = conId;
    }
    public void removeFollower(String name){
        followers.remove(name);
    }

    public void unFollow(String name){
        following.remove(name);
    }
    public void follow(String name){
        following.add(name);
    }

    public void addBlocked(String name){
        blocked.add(name);
    }

    public void removeBlocked(String name){
        blocked.remove(name);
    }

    public void addUnreadMessages(ArrayList<String> msg){
        UnreadMessages.add(msg);
    }

    public void addPost(Post post){
        myPosts.add(post);
    }

    public void addPm(PM pm){
        myPms.add(pm);
    }


    //QUERIES -------

    public boolean isFollower(String name){
        return followers.contains(name);
    }

    public boolean isFollowing(String name){
        return following.contains(name);
    }

    public boolean isBlocked(String name){
        return blocked.contains(name);
    }

    public boolean isLogedIn(){
        return loggedIn;
    }






}
