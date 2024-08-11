package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripmaster.R;
import com.example.tripmaster.Service.AuthService;

public class MainActivity extends AppCompatActivity implements IScreenSwitch {
    private AuthService authService;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button registerButton;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = new AuthService();

        initViews();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);
        errorTextView = findViewById(R.id.errorTextView);

        signInButton.setOnClickListener(v -> signInUser());
        registerButton.setOnClickListener(v -> registerUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        if (authService.getAuth().getCurrentUser() == null) {
            showSignInUI();
        } else {
            authService.checkUserExistence(authService.getAuth().getCurrentUser(), new AuthService.OnAuthCompleteListener() {
                @Override
                public void onSuccess() {
                    switchScreen();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("Auth", errorMessage);
                    errorTextView.setText(errorMessage);
                    errorTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void showSignInUI() {
        emailEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        authService.signInUser(email, password, new AuthService.OnAuthCompleteListener() {
            @Override
            public void onSuccess() {
                switchScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                errorTextView.setText(errorMessage);
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        authService.registerUser(email, password, new AuthService.OnAuthCompleteListener() {
            @Override
            public void onSuccess() {
                switchScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                errorTextView.setText(errorMessage);
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
