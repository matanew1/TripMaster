package com.example.tripmaster.Model;


import java.util.ArrayList;
import java.util.HashMap;

// Trip class
public class Trip {
    private int imageResId;
    private String title;
    private String location;
    private HashMap<String, ArrayList<EventTrip>> eventTrips;
    private String startDate;

    public Trip(int imageResId, String title, String location, String startDate) {
        this.imageResId = imageResId;
        this.title = title;
        this.location = location;
        this.eventTrips = new HashMap<>();
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public HashMap<String, ArrayList<EventTrip>> getEventTrips() {
        return eventTrips;
    }

    public void setEventTrips(HashMap<String, ArrayList<EventTrip>> eventTrips) {
        this.eventTrips = eventTrips;
    }
}