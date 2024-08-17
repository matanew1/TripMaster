package com.example.tripmaster.Data;

import android.util.Log;

import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.Service.DatabaseService;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressWarnings("all")
public class DataManager {
    private static DataManager instance;
    private ArrayList<Trip> trips;
    private DatabaseService databaseService;

    private DataManager() {
        trips = new ArrayList<>();
        databaseService = new DatabaseService();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public void initialize(FirebaseUser currentUser) {
        // Load user data from the database
        databaseService.loadUserData(currentUser, new DatabaseService.DataLoadCallback() {
            @Override
            public void onDataLoaded(UserDB userDB) {
                trips = userDB.getAllTrips();
                Log.d("DataManager", "User data loaded successfully");
            }

            @Override
            public void onDataLoadFailed(String error) {
                Log.e("DataManager", "Error loading user data: " + error);
            }
        });
    }

    public void addTrip(FirebaseUser currentUser, Trip trip) {
        if (!trips.contains(trip)) {
            trips.add(trip);
            // Save trip to the database
            databaseService.saveTrip(currentUser, trip);
        } else {
            Log.d("DataManager", "Trip already exists: " + trip.toString());
        }
    }
    public ArrayList<Trip> getTrips() {
        return new ArrayList<>(trips);  // Return a copy to avoid external modifications
    }

}
