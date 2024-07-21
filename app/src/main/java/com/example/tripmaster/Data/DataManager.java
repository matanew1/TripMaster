package com.example.tripmaster.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripmaster.Model.EventTrip;
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

    public void addTrip(Trip trip) {
        // Check if the trip already exists before adding
        if (!trips.contains(trip)) {
            trips.add(trip);
        } else {
            Log.d("DataManager", "Trip already exists: " + trip.toString());
        }
    }

    public ArrayList<EventTrip> getEventsForDate(String date) {
        for (Trip trip : trips) {
            if (trip.getStartDate().equals(date)) {
                // Combine all events from this trip
                ArrayList<EventTrip> events = new ArrayList<>();
                for (ArrayList<EventTrip> eventList : trip.getEventTrips().values()) {
                    events.addAll(eventList);
                }
                return events;
            }
        }
        return null;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public Trip getTripForDate(String date) {
        // This is a placeholder for the actual implementation
        // You might query your database or in-memory data to fetch the trip data for the given date
        for (Trip trip : trips) {  // Assume 'trips' is a collection of all trips
            if (trip.getStartDate().equals(date)) {
                return trip;
            }
        }
        return null;  // Return null if no trip is found for the given date
    }

}
