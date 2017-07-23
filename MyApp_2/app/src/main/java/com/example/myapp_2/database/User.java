package com.example.myapp_2.database;

/**
 * Created by W--Inarius on 2017/7/12.
 */

public class User {
    private String username;
    private String nickname;
    private int level;
    private String password;

    public User(String username, String nickname, int level, String password) {
        this.username = username;
        this.nickname = nickname;
        this.level = level;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }



    public String getNickname() {
        return nickname;
    }


    public int getLevel() {
        return level;
    }


    public String getPassword() {
        return password;
    }


}
