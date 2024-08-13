package com.example.tripmaster.Model;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDB implements Serializable {

    private static final long serialVersionUID = 1L; // Add serialVersionUID

    private Map<String, Trip> allTrips;
    private String name;
    private String email;
    private String photoUrl;
    private String since;// Add photo URL field
    private static UserDB userDB = null;

    private UserDB() {
        allTrips = new HashMap<>();
    }

    private UserDB(@NonNull FirebaseUser currentUser) {
        allTrips = new HashMap<>();
        this.name = Objects.requireNonNull(currentUser.getEmail()).split("@")[0];
        this.email = currentUser.getEmail();
        this.photoUrl = String.valueOf(currentUser.getPhotoUrl()); // Initialize photo URL field
        if (this.since == null) {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.since =  "Since: "+formatter.format(date);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "UserDB{" +
                "allTrips=" + allTrips +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public static UserDB getUserDB() {
        return userDB;
    }

    public static void setUserDB(UserDB userDB) {
        UserDB.userDB = userDB;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public static void init(FirebaseUser currentUser) {
        if (userDB == null) {
            userDB = new UserDB(currentUser);
        }
    }

    public static UserDB getInstance() {
        if (userDB == null) {
            throw new IllegalStateException("UserDB is not initialized. Call init() first.");
        }
        return userDB;
    }

    public static UserDB getCurrentUser() {
        return getInstance();
    }

    public void setUser(UserDB user) {
        this.name = user.name;
        this.email = user.email;
        this.photoUrl = user.photoUrl; // Set photo URL field
        this.allTrips = user.allTrips;
        this.since = user.since;
    }

    public ArrayList<Trip> getAllTrips() {
        return new ArrayList<>(allTrips.values());
    }

    public void setAllTrips(Map<String, Trip> allTrips) {
        this.allTrips = allTrips;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
