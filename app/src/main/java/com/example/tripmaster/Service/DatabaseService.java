package com.example.tripmaster.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.Utils.Consts;
import com.example.tripmaster.Utils.FireBaseOperations;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseService {

    private final DatabaseReference userDatabaseReference;

    public DatabaseService() {
        this.userDatabaseReference = FireBaseOperations.getInstance().getDatabaseReference(Consts.USER_DB);
    }

    public void loadUserData(@NonNull FirebaseUser currentUser, final DataLoadCallback callback) {
        String userId = currentUser.getUid();
        userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserDB userDB = snapshot.getValue(UserDB.class);
                    if (userDB != null) {
                        // Handle the conversion from Map to List
                        Map<String, Trip> tripsMap = (Map<String, Trip>) snapshot.child("allTrips").getValue();
                        if (tripsMap != null) {
                            userDB.setAllTrips(tripsMap);
                        }
                        UserDB.getInstance().setUser(userDB);
                        callback.onDataLoaded(userDB);
                    } else {
                        callback.onDataLoadFailed("User data is null");
                    }
                } else {
                    callback.onDataLoadFailed("User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseService", "Error loading user data: ", error.toException());
                callback.onDataLoadFailed(error.getMessage());
            }
        });
    }

    public void saveTrip(@NonNull FirebaseUser currentUser, @NonNull Trip trip) {
        String userId = currentUser.getUid();
        DatabaseReference tripRef = userDatabaseReference.child(userId).child("allTrips").child(trip.getId());

        // Save the Trip object
        tripRef.setValue(trip)
                .addOnSuccessListener(aVoid -> Log.d("DatabaseService", "Trip data saved successfully"))
                .addOnFailureListener(e -> Log.e("DatabaseService", "Error saving trip data: ", e));
    }

    public void loadTrips(@NonNull FirebaseUser currentUser, final TripsLoadCallback callback) {
        String userId = currentUser.getUid();
        userDatabaseReference.child(userId).child("allTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> tripsList = new ArrayList<>();
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    Trip trip = tripSnapshot.getValue(Trip.class);
                    if (trip != null) {
                        tripsList.add(trip);
                    }
                }
                callback.onTripsLoaded(tripsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseService", "Error loading trips: ", error.toException());
                callback.onTripsLoadFailed(error.getMessage());
            }
        });
    }

    public interface DataLoadCallback {
        void onDataLoaded(UserDB userDB);
        void onDataLoadFailed(String error);
    }

    public interface TripsLoadCallback {
        void onTripsLoaded(ArrayList<Trip> trips);
        void onTripsLoadFailed(String error);
    }
}
