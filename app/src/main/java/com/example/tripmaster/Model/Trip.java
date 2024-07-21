package com.example.tripmaster.Model;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// Trip class
public class Trip {

    private String id;
    private int imageResId;
    private String title;
    private String location;
    private HashMap<String, ArrayList<EventTrip>> eventTrips;
    private String startDate;

    public Trip(int imageResId, String title, String location, String startDate) {
        this.id = String.valueOf(UUID.randomUUID());
        this.imageResId = imageResId;
        this.title = title;
        this.location = location;
        this.eventTrips = new HashMap<>();
        this.startDate = startDate;
    }

    public Trip() {
        this.id = String.valueOf(UUID.randomUUID());
        this.imageResId = -1;
        this.title = "";
        this.location = "";
        this.eventTrips = new HashMap<>();
        this.startDate = "";
    }

    public String getStartDate() {
        return startDate;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id.equals(trip.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @NonNull
    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", imageResId=" + imageResId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", eventTrips=" + eventTrips +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}