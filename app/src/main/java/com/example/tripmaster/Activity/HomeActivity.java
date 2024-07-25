package com.example.tripmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements IScreenSwitch {

    private RecyclerView recyclerView;
    private ArrayList<Trip> tripList;
    private FirebaseAuth firebaseAuth;
    private TripAdapter tripAdapter;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseService = new DatabaseService(); // Initialize DatabaseService

        initializeViews();
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadMenuFragment();
        }

        // Load trips from DatabaseService
        loadTrips();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.trips_list);
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(btn -> logout());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(this, tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    private void loadTrips() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            databaseService.loadTrips(currentUser, new DatabaseService.TripsLoadCallback() {
                @Override
                public void onTripsLoaded(ArrayList<Trip> trips) {
                    System.out.println("EEEEEEEEEEEEEEEEEEEE " + trips);
                    // Update tripList and notify the adapter
                    tripList.clear();
                    tripList.addAll(trips);
                    tripAdapter.notifyDataSetChanged();
                }

                @Override
                public void onTripsLoadFailed(String error) {
                    // Handle the error, e.g., show a toast or log it
                    Log.e("HomeActivity", "Error loading trips: " + error);
                }
            });
        } else {
            // Handle case when user is not logged in
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
}
