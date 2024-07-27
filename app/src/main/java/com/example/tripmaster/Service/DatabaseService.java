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
        fetchData(userDatabaseReference.child(userId), snapshot -> {
            UserDB userDB = snapshot.getValue(UserDB.class);
            if (userDB != null) {
                callback.onDataLoaded(userDB);
            } else {
                callback.onDataLoadFailed("User data is null");
            }
        }, error -> {
            Log.e("DatabaseService", "Error loading user data: ", error.toException());
            callback.onDataLoadFailed(error.getMessage());
        });
    }

    public void saveTrip(@NonNull FirebaseUser currentUser, @NonNull Trip trip) {
        String userId = currentUser.getUid();
        updateData(userDatabaseReference.child(userId).child("allTrips").child(trip.getId()), trip,
                "Trip data saved successfully", "Error saving trip data");
    }

    public void loadMyTrips(@NonNull FirebaseUser currentUser, final TripsLoadCallback callback) {
        fetchData(userDatabaseReference.child(currentUser.getUid()).child("allTrips"), snapshot -> {
            List<Trip> tripsList = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                    .map(tripSnapshot -> tripSnapshot.getValue(Trip.class))
                    .collect(Collectors.toList());
            callback.onTripsLoaded(new ArrayList<>(tripsList));
        }, error -> {
            Log.e("DatabaseService", "Error loading trips: ", error.toException());
            callback.onTripsLoadFailed(error.getMessage());
        });
    }

    public void loadGlobalTrips(final TripsLoadCallback callback) {
        fetchData(userDatabaseReference, snapshot -> {
            List<Trip> globalTripsList = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                    .flatMap(userSnapshot -> StreamSupport.stream(userSnapshot.child("allTrips").getChildren().spliterator(), false)
                            .map(tripSnapshot -> tripSnapshot.getValue(Trip.class))
                            .filter(trip -> trip != null && trip.isShared())
                    )
                    .collect(Collectors.toList());
            callback.onTripsLoaded(new ArrayList<>(globalTripsList));
        }, error -> {
            Log.e("DatabaseService", "Error loading trips: ", error.toException());
            callback.onTripsLoadFailed(error.getMessage());
        });
    }

    public void updateGlobalTrip(@NonNull Trip trip) {
        String tripId = trip.getId();
        fetchData(userDatabaseReference, snapshot -> {
            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                if (userSnapshot.child("allTrips").hasChild(tripId)) {
                    updateData(userSnapshot.getRef().child("allTrips").child(tripId), trip,
                            "Global trip updated successfully for user: " + userSnapshot.getKey(),
                            "Error updating global trip for user: " + userSnapshot.getKey());
                }
            }
        }, error -> {
            Log.e("DatabaseService", "Error updating global trip: ", error.toException());
        });
    }

    private void fetchData(@NonNull DatabaseReference reference, DataLoadAction onDataLoaded, ErrorAction onError) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onDataLoaded.onData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onError.onError(error);
            }
        });
    }

    private void updateData(@NonNull DatabaseReference reference, Object data, String successMessage, String errorMessage) {
        reference.setValue(data)
                .addOnSuccessListener(aVoid -> Log.d("DatabaseService", successMessage))
                .addOnFailureListener(e -> Log.e("DatabaseService", errorMessage, e));
    }

    public interface DataLoadCallback {
        void onDataLoaded(UserDB userDB);
        void onDataLoadFailed(String error);
    }

    public interface TripsLoadCallback {
        void onTripsLoaded(ArrayList<Trip> trips);
        void onTripsLoadFailed(String error);
    }

    private interface DataLoadAction {
        void onData(DataSnapshot snapshot);
    }

    private interface ErrorAction {
        void onError(DatabaseError error);
    }
}
