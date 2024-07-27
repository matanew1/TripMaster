package com.example.tripmaster.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Activity.Fragments.RatingFragment;
import com.example.tripmaster.Adapter.DaysTripAdapter;
import com.example.tripmaster.Adapter.EventTripViewAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TripViewActivity extends AppCompatActivity implements IScreenSwitch, DaysTripAdapter.OnDateClickListener {

    private static final String TAG = "TripViewActivity"; // Tag for logging

    private Trip currentTrip;
    private EventTripViewAdapter eventTripViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data);

        // Initialize currentTrip
        if (getIntent() != null && getIntent().hasExtra("clicked_trip")) {
            currentTrip = (Trip) getIntent().getSerializableExtra("clicked_trip");
        } else {
            currentTrip = new Trip(); // Or show an error
        }

        if (savedInstanceState == null) {
            loadMenuFragment();
            loadRatingFragment();
        }

        RatingBar myRatingSlider = findViewById(R.id.rating_bar);
        myRatingSlider.setRating(currentTrip.getAverageRating());
        myRatingSlider.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                Log.d(TAG, "New average rating: " + rating);
                refreshRatingFragment(rating);
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Update the trip's average rating
            currentTrip.updateCurrentAverageRating(myRatingSlider.getRating());

            // Get the current FirebaseUser
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Log.e(TAG, "User not logged in");
                Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the trip data
            DataManager dataManager = DataManager.getInstance();
            dataManager.getDatabaseService().saveTrip(currentUser, currentTrip);
            Log.d(TAG, "Updated average rating: " + currentTrip.getAverageRating());

            // Switch to the HomeActivity
            switchScreen();
        });

        RecyclerView recyclerViewDays = findViewById(R.id.recyclerViewDays);
        RecyclerView recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        // Set up RecyclerView for Days
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> eventDates = new ArrayList<>(currentTrip.getEventTrips().keySet());
        DaysTripAdapter daysTripAdapter = new DaysTripAdapter(eventDates, this);
        recyclerViewDays.setAdapter(daysTripAdapter);

        // Set up RecyclerView for Events
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        eventTripViewAdapter = new EventTripViewAdapter(new ArrayList<>());
        recyclerViewEvents.setAdapter(eventTripViewAdapter);

        // Load events for the default selected date
        if (!eventDates.isEmpty()) {
            String defaultDate = eventDates.get(0);
            ArrayList<EventTrip> events = new ArrayList<>(currentTrip.getEventTrips().getOrDefault(defaultDate, new ArrayList<>()));
            eventTripViewAdapter.updateEvents(events);
        }
    }

    private void refreshRatingFragment(float rating) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RatingFragment fragment = (RatingFragment) fragmentManager.findFragmentById(R.id.rating_frag);
        if (fragment != null) {
            fragment.updateRating(rating);
        } else {
            Log.d(TAG, "RatingFragment is null");
        }
    }


    @Override
    public void onDateClick(String date) {
        Log.d(TAG, "Selected date: " + date); // Log the selected date

        ArrayList<EventTrip> events = new ArrayList<>(currentTrip.getEventTrips().getOrDefault(date, new ArrayList<>()));
        Log.d(TAG, "Events for date: " + events); // Log events to verify

        eventTripViewAdapter.updateEvents(events);
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadMenuFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_container, new MenuFragment())
                .commit();
    }


    private void loadRatingFragment() {
        RatingFragment ratingFragment = RatingFragment.newInstance(currentTrip);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rating_frag, ratingFragment)
                .commit();
    }
}
