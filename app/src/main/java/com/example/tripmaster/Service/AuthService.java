package com.example.tripmaster.Service;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.example.tripmaster.Activity.IScreenSwitch;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.R;
import com.example.tripmaster.Utils.Consts;
import com.example.tripmaster.Utils.FireBaseOperations;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AuthService {
    private final DatabaseReference reference;
    private boolean isNewUser;
    private final ActivityResultLauncher<Intent> signInLauncher;
    private final IScreenSwitch IScreenSwitch;



    public AuthService(ActivityResultLauncher<Intent> signInLauncher, IScreenSwitch listener) {
        this.reference = FireBaseOperations.getInstance().getDatabaseReference(Consts.USER_DB);
        this.signInLauncher = signInLauncher;
        this.IScreenSwitch = listener;
    }

    public void login(FirebaseUser currentUser) {
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

    private void checkAlreadyExists(FirebaseUser currentUser) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isNewUser = true;

                if (!dataSnapshot.exists()) {
                    createNewUserDB(currentUser);
                    IScreenSwitch.switchScreen();
                } else {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (currentUser.getUid().equals(snap.getKey())) {
                            loadUserFromDB(currentUser);
                            isNewUser = false;
                            break;
                        }
                    }

                    if (isNewUser) {
                        createNewUserDB(currentUser);
                    }
                    IScreenSwitch.switchScreen();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving data: " + error.getMessage());
            }
        });
    }

    private void createNewUserDB(FirebaseUser currentUser) {
        UserDB.getInstance().setName(currentUser.getDisplayName());
        reference.child(currentUser.getUid()).setValue(UserDB.getInstance());
    }

    private void loadUserFromDB(FirebaseUser currentUser) {
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDB.getInstance().setUser(Objects.requireNonNull(snapshot.getValue(UserDB.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading user: " + error.getMessage());
            }
        });
    }
}
