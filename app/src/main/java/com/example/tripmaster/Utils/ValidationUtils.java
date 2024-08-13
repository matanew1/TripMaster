package com.example.tripmaster.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ValidationUtils {

    public static boolean validateEmail(String email, TextInputLayout emailInputLayout) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            return false;
        } else if (!email.matches(emailPattern)) {
            emailInputLayout.setError("Invalid email format");
            return false;
        } else {
            emailInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validatePassword(String password, TextInputLayout passwordInputLayout) {
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters long");
            return false;
        } else {
            passwordInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validateUserLoggedIn(Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(context, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
