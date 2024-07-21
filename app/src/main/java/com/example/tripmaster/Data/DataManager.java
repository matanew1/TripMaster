package com.example.tripmaster.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void addTrip(Trip trip) {
        if (!trips.contains(trip)) {
            trips.add(trip);
        } else {
            Log.d("DataManager", "Trip already exists: " + trip.toString());
        }
    }

    public void updateTrip(Trip updatedTrip) {
        for (int i = 0; i < trips.size(); i++) {
            Trip existingTrip = trips.get(i);
            if (existingTrip.getId().equals(updatedTrip.getId())) {
                trips.set(i, updatedTrip);
                Log.d("DataManager", "Trip updated: " + updatedTrip);
                return;
            }
        }
        Log.d("DataManager", "Trip not found for update: " + updatedTrip.toString());
    }

    public ArrayList<EventTrip> getEventsForDate(String date) {
        for (Trip trip : trips) {
            if (trip.getEventTrips().containsKey(date)) {
                // Combine all events from this date
                ArrayList<EventTrip> events = new ArrayList<>();
                events.addAll(trip.getEventTrips().get(date));
                return events;
            }
        }
        return new ArrayList<>();  // Return an empty list if no events are found
    }

    public ArrayList<Trip> getTrips() {
        return new ArrayList<>(trips);  // Return a copy to avoid external modifications
    }

    public Trip getTripForDate(String date) {
        for (Trip trip : trips) {
            if (trip.getStartDate().equals(date)) {
                return trip;
            }
        }
        return null;  // Return null if no trip is found for the given date
    }

    public Trip getTripById(String tripId) {
        for (Trip trip : trips) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
        }
        return null;  // Return null if no trip is found for the given ID
    }
}
