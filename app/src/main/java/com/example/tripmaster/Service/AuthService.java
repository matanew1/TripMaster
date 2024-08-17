package com.example.tripmaster.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.Utils.Consts;
import com.example.tripmaster.Utils.FireBaseOperations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("all")
public class AuthService {
    private DatabaseReference reference;
    private FirebaseAuth mAuth;


    public FirebaseAuth getAuth() {return mAuth;}
    public AuthService() {
        mAuth = FirebaseAuth.getInstance();
        reference = FireBaseOperations.getInstance().getDatabaseReference(Consts.USER_DB);
    }

    public void signInUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserDB.init(user);
                            checkUserExistence(user, listener);
                        }
                    } else {
                        Log.w("Auth", "Sign-in failed", task.getException());
                        listener.onFailure("Sign-in failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void registerUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserDB.init(user);
                            createNewUserDB(user, listener);
                        }
                    } else {
                        Log.w("Auth", "Registration failed", task.getException());
                        listener.onFailure("Registration failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void checkUserExistence(@NonNull FirebaseUser currentUser, OnAuthCompleteListener listener) {
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadUserFromDB(currentUser, listener);
                } else {
                    createNewUserDB(currentUser, listener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving data: " + error.getMessage());
                listener.onFailure("Error retrieving data: " + error.getMessage());
            }
        });
    }

    private void createNewUserDB(@NonNull FirebaseUser currentUser, OnAuthCompleteListener listener) {
        UserDB userDB = UserDB.getInstance();
        userDB.setName(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "No Name");
        userDB.setEmail(currentUser.getEmail() != null ? currentUser.getEmail() : "No Email");
        reference.child(currentUser.getUid()).setValue(userDB).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onSuccess();
            } else {
                Log.e("FirebaseError", "Error creating new user: " + task.getException().getMessage());
                listener.onFailure("Error creating new user: " + task.getException().getMessage());
            }
        });
    }

    private void loadUserFromDB(@NonNull FirebaseUser currentUser, OnAuthCompleteListener listener) {
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserDB.init(currentUser);
                UserDB userDB = UserDB.getInstance();
                if (userDB != null) {
                    // Check if 'allTrips' is a Map or List
                    Object tripsObject = snapshot.child("allTrips").getValue();
                    if (tripsObject instanceof Map) {
                        Map<String, Trip> tripsMap = (Map<String, Trip>) tripsObject;
                        userDB.setAllTrips(tripsMap);
                    } else if (tripsObject instanceof List) {
                        List<Trip> tripsList = (List<Trip>) tripsObject;
                        Map<String, Trip> tripsMap = new HashMap<>();
                        for (Trip trip : tripsList) {
                            tripsMap.put(trip.getId(), trip); // Adjust according to how you identify each trip
                        }
                        userDB.setAllTrips(tripsMap);
                    }
                    userDB.setEmail(currentUser.getEmail());
                    userDB.setPhotoUrl(String.valueOf(currentUser.getPhotoUrl()));
                    userDB.setSince(Objects.requireNonNull(snapshot.child("since").getValue()).toString());
                    listener.onSuccess();
                } else {
                    listener.onFailure("UserDB instance is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading user: " + error.getMessage());
                listener.onFailure("Error loading user: " + error.getMessage());
            }
        });
    }

    public interface OnAuthCompleteListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
