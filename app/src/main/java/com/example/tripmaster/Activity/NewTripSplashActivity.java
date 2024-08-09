package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.tripmaster.R;

@SuppressLint("CustomSplashScreen")
public class NewTripSplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 5000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new_trip);

        // Find the LottieAnimationView by its ID
        LottieAnimationView animationView = findViewById(R.id.animationViewNewTrip);

        // Play the animation and loop it
        animationView.playAnimation();
        animationView.setRepeatCount(LottieDrawable.INFINITE);

        // Schedule the transition to the next activity after 5 seconds
        new Handler().postDelayed(this::startNextActivity, SPLASH_DURATION_MS);
    }

    private void startNextActivity() {
        // Stop the animation to free up resources
        LottieAnimationView animationView = findViewById(R.id.animationViewNewTrip);
        animationView.cancelAnimation();

        // Proceed to the next activity (e.g., MainActivity)
        Intent intent = new Intent(NewTripSplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Finish the splash activity so the user can't go back to it
    }
}
