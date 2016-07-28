package project3.wenproject3.model;

import java.util.UUID;

/**
 * Created by zhouwen on 6/15/16.
 */
public class User {
    private String name;
    private String username;
    private String password;
    private int posts;
    private int followers;
    private int followings;
    private String profilePic = "user";

    public User (String username, String password){
        this.username = username;
        this.password = password;
        this.posts = 0;
        this.followers =0;
        this.followings=0;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getName (){
        return name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password =password;
    }

    public String getPassword(){
        return password;
    }

    public void setPosts(int posts){
        this.posts =posts;
    }

    public int getPosts(){
        return posts;
    }

    public void setFollowers(int followers){
        this.followers = followers;
    }

    public int getFollowers(){
        return followers;
    }

    public void setFollowings (int followings){
        this.followings = followings;
    }

    public int getFollowings(){
        return followings;
    }

    public void setProfilePic(String profilePic){
        this.profilePic = profilePic;
    }

    public String getProfilePic(){
        return profilePic;
    }
}
