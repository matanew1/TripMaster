package com.example.tripmaster;


// Trip class
public class Trip {
    private int imageResId;
    private String title;
    private String location;
    private String date;

    public Trip(int imageResId, String title, String location, String date) {
        this.imageResId = imageResId;
        this.title = title;
        this.location = location;
        this.date = date;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }
}