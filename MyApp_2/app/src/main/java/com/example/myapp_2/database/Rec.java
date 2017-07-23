package com.example.myapp_2.database;

/**
 * Created by W--Inarius on 2017/7/11.
 */

public class Rec {
    private String time;
    private String name;
    private String mission;

    public Rec(String time, String name,String mission) {
        this.time = time;
        this.name = name;
        this.mission = mission;
    }

    public String getMission() {
        return mission;
    }
    public String getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
}
