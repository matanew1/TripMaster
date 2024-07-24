package com.example.tripmaster.Model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserDB {

    private ArrayList<Trip> allTrips;
    private String name;
    private static UserDB userDB = null;

    private UserDB() {
        allTrips = new ArrayList<>();
    }

    private UserDB(FirebaseUser currentUser) {
        allTrips = new ArrayList<>();
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
        return allTrips;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTrip(Trip trip) {
        allTrips.add(0, trip);
    }

    public void setAllTrips(ArrayList<Trip> allTrips) {
        this.allTrips = allTrips;
    }
}
