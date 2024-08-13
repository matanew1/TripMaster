package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripmaster.R;
import com.example.tripmaster.Service.AuthService;
import com.example.tripmaster.Utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IScreenSwitch {
    private AuthService authService;

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
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
        emailInputLayout = (TextInputLayout) emailEditText.getParent().getParent();
        passwordInputLayout = (TextInputLayout) passwordEditText.getParent().getParent();

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
        showSignInUI();
    }

    private void showSignInUI() {
        emailEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE); // Hide error text view initially
    }

    private boolean validateFields() {
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

        boolean isEmailValid = ValidationUtils.validateEmail(email, emailInputLayout);
        boolean isPasswordValid = ValidationUtils.validatePassword(password, passwordInputLayout);

        return isEmailValid && isPasswordValid;
    }

    @SuppressLint("SetTextI18n")
    private void signInUser() {
        if (!validateFields()) {
            return;
        }

        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

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
        if (!validateFields()) {
            return;
        }

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
