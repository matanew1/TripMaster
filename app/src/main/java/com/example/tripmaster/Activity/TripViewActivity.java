package com.example.tripmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Import logging
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.DaysTripAdapter;
import com.example.tripmaster.Adapter.EventTripViewAdapter;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class TripViewActivity extends AppCompatActivity implements IScreenSwitch, DaysTripAdapter.OnDateClickListener {

    private static final String TAG = "TripViewActivity"; // Tag for logging

    private RecyclerView recyclerViewDays;
    private RecyclerView recyclerViewEvents;
    private Trip currentTrip;
    private Button backButton;
    private DaysTripAdapter daysTripAdapter;
    private EventTripViewAdapter eventTripViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data);

        // Initialize currentTrip
        if (getIntent() != null && getIntent().hasExtra("clicked_trip")) {
            currentTrip = (Trip) getIntent().getSerializableExtra("clicked_trip");
        } else {
            // Handle the case where currentTrip is null
            currentTrip = new Trip(); // Or show an error
        }

        if (savedInstanceState == null) {
            loadMenuFragment();
        }

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> switchScreen());

        recyclerViewDays = findViewById(R.id.recyclerViewDays);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        // Set up RecyclerView for Days
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> eventDates = new ArrayList<>(currentTrip.getEventTrips().keySet());
        daysTripAdapter = new DaysTripAdapter(eventDates, this);
        recyclerViewDays.setAdapter(daysTripAdapter);

        // Set up RecyclerView for Events
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        eventTripViewAdapter = new EventTripViewAdapter(new ArrayList<>());
        recyclerViewEvents.setAdapter(eventTripViewAdapter);
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
}
