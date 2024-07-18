package com.example.tripmaster.Data;

import androidx.annotation.NonNull;

import com.example.tripmaster.Model.Trip;

import java.util.ArrayList;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Trip> trips;

    private DataManager() {
        trips = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public int getSize() {return trips.size();}

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip event) {
        trips.add(event);
    }

    @NonNull
    @Override
    public String toString() {
        return "DataManager{" +
                "trips=" + trips +
                '}';
    }

    public void updateTrip(Trip updatedTrip) {
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            if (trip.getStartDate().equals(updatedTrip.getStartDate())) {
                trips.set(i, updatedTrip);
                return; // Exit loop once updated
            }
        }
    }
}
