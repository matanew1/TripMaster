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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        userDatabaseReference.child(userId).child("allTrips").child(trip.getId()).setValue(trip)
                .addOnSuccessListener(aVoid -> Log.d("DatabaseService", "Trip data saved successfully"))
                .addOnFailureListener(e -> Log.e("DatabaseService", "Error saving trip data: ", e));
    }

    public void loadMyTrips(@NonNull FirebaseUser currentUser, final TripsLoadCallback callback) {
        userDatabaseReference.child(currentUser.getUid()).child("allTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Trip> tripsList = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                        .map(tripSnapshot -> tripSnapshot.getValue(Trip.class))
                        .collect(Collectors.toList());
                callback.onTripsLoaded(new ArrayList<>(tripsList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseService", "Error loading trips: ", error.toException());
                callback.onTripsLoadFailed(error.getMessage());
            }
        });
    }

    public void loadGlobalTrips(final TripsLoadCallback callback) {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Stream through all user snapshots and their trips
                List<Trip> globalTripsList = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                        .flatMap(userSnapshot -> StreamSupport.stream(userSnapshot.child("allTrips").getChildren().spliterator(), false)
                                .map(tripSnapshot -> tripSnapshot.getValue(Trip.class))
                                .filter(trip -> trip != null && trip.isShared())
                        )
                        .collect(Collectors.toList());

                // Pass the filtered list to the callback
                callback.onTripsLoaded(new ArrayList<>(globalTripsList));
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
