package com.example.tripmaster.Model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDB implements Serializable {

    private Map<String, Trip> allTrips;  // Change to Map for Firebase deserialization
    private String name;
    private static UserDB userDB = null;

    private UserDB() {
        allTrips = new HashMap<>();  // Initialize as HashMap
    }

    private UserDB(FirebaseUser currentUser) {
        allTrips = new HashMap<>();  // Initialize as HashMap
        this.name = currentUser.getDisplayName();
    }

    public static void init(FirebaseUser currentUser) {
        if (userDB == null) {
            userDB = new UserDB(currentUser);
        }
    }

    public static UserDB getInstance() {
        if (userDB == null) {
            throw new IllegalStateException("UserDB is not initialized. Call init() first.");
        }
        return userDB;
    }

    public void setUser(UserDB user) {
        this.name = user.name;
        this.allTrips = user.allTrips;
    }

    public ArrayList<Trip> getAllTrips() {
        return new ArrayList<>(allTrips.values());  // Convert Map to List
    }

    public void setAllTrips(Map<String, Trip> allTrips) {
        this.allTrips = allTrips;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTrip(Trip trip) {
        allTrips.put(trip.getId(), trip);  // Ensure Trip has a unique ID
    }
}
