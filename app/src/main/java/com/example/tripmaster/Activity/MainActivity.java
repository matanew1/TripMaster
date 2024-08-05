package com.example.tripmaster.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.AuthService;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements IScreenSwitch {
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseApp if needed
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
        } else {
            FirebaseApp.initializeApp(this);
        }

        // Initialize AuthService
        authService = new AuthService(signInLauncher, this);
    }

    // Register ActivityResultLauncher for FirebaseAuthUIActivityResultContract
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    // Handle result from the sign-in flow
    private void onSignInResult(@NonNull FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                UserDB.init(user);
                authService.checkAlreadyExists(user);
            }
        } else {
            if (response == null) {
                // Sign-in was canceled by the user
                Log.d("Auth", "Sign-in cancelled by user.");
            } else {
                // Handle sign-in errors
                Exception exception = response.getError();
                if (exception != null) {
                    Log.e("Auth", "Sign-in failed: " + exception.getMessage());
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        authService.login();
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}