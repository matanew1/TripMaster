package com.example.tripmaster.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
        }

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> switchScreen());

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
