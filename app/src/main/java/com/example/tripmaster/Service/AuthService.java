package com.example.tripmaster.Service;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.example.tripmaster.Activity.IScreenSwitch;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.R;
import com.example.tripmaster.Utils.Consts;
import com.example.tripmaster.Utils.FireBaseOperations;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private final DatabaseReference reference;
    private final ActivityResultLauncher<Intent> signInLauncher;
    private final IScreenSwitch screenSwitchListener;

    public AuthService(ActivityResultLauncher<Intent> signInLauncher, IScreenSwitch listener) {
        this.reference = FireBaseOperations.getInstance().getDatabaseReference(Consts.USER_DB);
        this.signInLauncher = signInLauncher;
        this.screenSwitchListener = listener;
    }

    public void login() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.AnonymousBuilder().build()
            );

            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.firebaseTheme)
                    .setLogo(R.drawable.logo)
                    .setIsSmartLockEnabled(false)
                    .build();

            signInLauncher.launch(signInIntent);
        } else {
            UserDB.init(currentUser);
            checkAlreadyExists(currentUser);
        }
    }

    private void checkAlreadyExists(@NonNull FirebaseUser currentUser) {
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadUserFromDB(currentUser);
                } else {
                    createNewUserDB(currentUser);
                }
                screenSwitchListener.switchScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving data: " + error.getMessage());
            }
        });
    }

    private void createNewUserDB(@NonNull FirebaseUser currentUser) {
        UserDB userDB = UserDB.getInstance();
        userDB.setName(currentUser.getDisplayName());
        userDB.setEmail(currentUser.getEmail()); // Set email field
        reference.child(currentUser.getUid()).setValue(userDB);
    }

    private void loadUserFromDB(@NonNull FirebaseUser currentUser) {
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDB userDB = snapshot.getValue(UserDB.class);
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
                    UserDB.getInstance().setUser(userDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading user: " + error.getMessage());
            }
        });
    }
}
