package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.DatabaseService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("NotifyDataSetChanged")
public class HomeActivity extends AppCompatActivity implements IScreenSwitch {

    private RecyclerView recyclerView;
    private ArrayList<Trip> tripList;
    private ArrayList<Trip> allTrips; // Backup list for all trips
    private FirebaseAuth firebaseAuth;
    private TripAdapter tripAdapter;
    private DataManager dataManager;
    private Button globalTripsBtn, myTripsBtn;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeFirebase();
        initializeViews();
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadMenuFragment();
        }

        setupButtonListeners();
        setupSearchBarListener();

        setGlobalTrips();
        setButtonState(globalTripsBtn, myTripsBtn);
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        dataManager = DataManager.getInstance();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.trips_list);
        globalTripsBtn = findViewById(R.id.global_trips);
        myTripsBtn = findViewById(R.id.my_trips);
        searchBar = findViewById(R.id.search_bar);
        findViewById(R.id.logout_button).setOnClickListener(v -> logout());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        allTrips = new ArrayList<>();
        tripAdapter = new TripAdapter(this, tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    private void setupButtonListeners() {
        myTripsBtn.setOnClickListener(v -> {
            searchBar.setText("");
            setMyTrips();
            setButtonState(myTripsBtn, globalTripsBtn);
        });

        globalTripsBtn.setOnClickListener(v -> {
            searchBar.setText("");
            setGlobalTrips();
            setButtonState(globalTripsBtn, myTripsBtn);
        });
    }

    private void setupSearchBarListener() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterTrips(String query) {
        if (query.isEmpty()) {
            tripAdapter.updateTrips(new ArrayList<>(allTrips));
        } else {
            ArrayList<Trip> filteredTrips = new ArrayList<>();
            for (Trip trip : allTrips) {
                if (trip.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        trip.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    filteredTrips.add(trip);
                }
            }
            tripAdapter.updateTrips(filteredTrips);
        }
    }

    private void setButtonState(Button selectedButton, Button otherButton) {
        selectedButton.setBackgroundColor(getResources().getColor(R.color.button_default, getResources().newTheme()));
        otherButton.setBackgroundColor(getResources().getColor(R.color.white, getResources().newTheme()));
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
            allTrips.clear();
            allTrips.addAll(trips);
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
