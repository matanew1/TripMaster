package com.example.tripmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements IScreenSwitch {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;
    private DataManager dataManager;
    private FirebaseAuth firebaseAuth;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        dataManager = DataManager.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        initializeViews();
        loadTrips();
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadMenuFragment();
        }
    }

    /**
     * Initializes the RecyclerView.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.trips_list);
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(btn -> logout());
    }

    private void logout() {
        firebaseAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        switchScreen();
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripAdapter = new TripAdapter(tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    /**
     * Loads trip data into the trip list.
     */
    private void loadTrips() {
        tripList = new ArrayList<>();
        tripList = dataManager.getTrips();

    }

    /**
     * Loads the MenuFragment into the specified container.
     */
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
