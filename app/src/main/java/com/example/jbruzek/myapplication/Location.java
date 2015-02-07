package com.example.jbruzek.myapplication;

/**
 * Created by jbruzek on 2/7/15.
 */
public class Location {
    private String title = "";
    private float longitude = 0;
    private float latitude = 0;
    private int fullness = 0;
    private String description = "";

    public Location() {
        //empty constructor
    }

    public Location(String title) {
        this.title = title;
    }

    public Location(String title, float longitude, float latitude) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String title, int fullness) {
        this.title = title;
        this.fullness = fullness;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public void description(String des) {
        this.description = des;
    }

    public int fullness() {
        return fullness;
    }
}
