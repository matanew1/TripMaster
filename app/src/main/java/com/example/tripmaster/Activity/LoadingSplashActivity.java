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
public class LoadingSplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_loading);

        // Find the LottieAnimationView by its ID
        LottieAnimationView animationView = findViewById(R.id.animationView);

        // Play the animation and loop it
        animationView.playAnimation();
        animationView.setRepeatCount(LottieDrawable.INFINITE);

        // Schedule the transition to the next activity after 5 seconds
        new Handler().postDelayed(this::startNextActivity, SPLASH_DURATION_MS);
    }

    private void startNextActivity() {
        // Stop the animation to free up resources
        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.cancelAnimation();

        // Proceed to the next activity (e.g., MainActivity)
        Intent intent = new Intent(LoadingSplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the splash activity so the user can't go back to it
    }
}
