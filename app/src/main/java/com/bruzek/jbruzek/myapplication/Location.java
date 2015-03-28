package com.bruzek.jbruzek.myapplication;

/**
 * Created by jbruzek on 2/7/15.
 */
public class Location {
    private String title = "";
    private int fullness = 0;
    private double rating = 0.0;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String description = "";
    private String id = "";

    public Location() {
        //empty constructor
    }

    public Location(String title) {
        this.title = title;
    }

    public Location(String title, double longitude, double latitude) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String title, int fullness) {
        this.title = title;
        this.fullness = fullness;
    }

    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public void title(String title) {
        this.title = title;
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

    public void fullness(int fullness) {
        this.fullness = fullness;
    }

    public void rating(double rating) {
        this.rating = rating;
    }

    public double rating() {
        return rating;
    }

    public void latitude(double latitude) {
        this.latitude = latitude;
    }

    public double latitude() {
        return latitude;
    }

    public void longitude(double longitude) {
        this.longitude = longitude;
    }

    public double longitude() {
        return longitude;
    }
}