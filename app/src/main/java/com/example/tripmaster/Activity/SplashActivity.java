package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripmaster.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View splashImage = findViewById(R.id.splash_image);

        // Load pendulum animation
        Animation pendulum = AnimationUtils.loadAnimation(this, R.anim.pendulum);
        splashImage.startAnimation(pendulum);

        // Start MainActivity after animation ends
        splashImage.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // Adjust delay to match or exceed animation duration
    }
}
