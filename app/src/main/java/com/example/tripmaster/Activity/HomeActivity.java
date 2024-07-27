package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("NotifyDataSetChanged")
public class HomeActivity extends AppCompatActivity implements IScreenSwitch {

    private RecyclerView recyclerView;
    private ArrayList<Trip> tripList;
    private FirebaseAuth firebaseAuth;
    private TripAdapter tripAdapter;
    private DataManager dataManager;
    private Button globalTripsBtn, myTripsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        dataManager = DataManager.getInstance();

        initializeViews();
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadMenuFragment();
        }

        myTripsBtn.setOnClickListener(v -> {
            setMyTrips();
            setButtonState(myTripsBtn, globalTripsBtn);
        });
        globalTripsBtn.setOnClickListener(v -> {
            setGlobalTrips();
            setButtonState(globalTripsBtn, myTripsBtn);
        });
        setGlobalTrips();
        setButtonState(globalTripsBtn, myTripsBtn);
    }

    private void setButtonState(Button selectedButton, Button otherButton) {
        selectedButton.setBackgroundColor(getResources().getColor(R.color.button_default, getResources().newTheme()));
        otherButton.setBackgroundColor(getResources().getColor(R.color.white, getResources().newTheme()));
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.trips_list);
        findViewById(R.id.logout_button).setOnClickListener(v -> logout());
        globalTripsBtn = findViewById(R.id.global_trips);
        myTripsBtn = findViewById(R.id.my_trips);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(this, tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    private void setMyTrips() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            dataManager.getDatabaseService().loadMyTrips(currentUser, new TripsLoadCallback());
        } else {
            Log.w("HomeActivity", "No user is currently logged in.");
        }
    }

    private void setGlobalTrips() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            dataManager.getDatabaseService().loadGlobalTrips(new TripsLoadCallback());
        } else {
            Log.w("HomeActivity", "No user is currently logged in.");
        }
    }

    private void logout() {
        firebaseAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        switchScreen();
    }

    private void loadMenuFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_container, new MenuFragment())
                .commit();
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class TripsLoadCallback implements DatabaseService.TripsLoadCallback {
        @Override
        public void onTripsLoaded(ArrayList<Trip> trips) {
            tripList.clear();
            tripList.addAll(trips);
            tripAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTripsLoadFailed(String error) {
            Log.e("HomeActivity", "Error loading trips: " + error);
        }
    }
}
