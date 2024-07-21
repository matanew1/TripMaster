package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Adapter.EventTripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Utils.DatePickerHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddTripActivity extends AppCompatActivity implements ScreenSwitchListener {

    private RecyclerView recyclerView;
    private ArrayList<EventTrip> eventList;
    private EventTripAdapter eventAdapter;
    private TextInputEditText etDate;
    private DataManager dataManager;
    private Button cancelBtn, saveBtn;
    private EditText titleTrip, countryTrip;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        dataManager = DataManager.getInstance();
        currentTrip = new Trip();  // Initialize currentTrip once

        initializeViews();
        setupRecyclerView();
        loadEventTrips();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.event_trips);
        etDate = findViewById(R.id.etDate);
        titleTrip = findViewById(R.id.title_trip);
        countryTrip = findViewById(R.id.country_trip);
        cancelBtn = findViewById(R.id.cancel_button);
        saveBtn = findViewById(R.id.save_btn);

        setupCancelButton();
        setupSaveButton();
        setupDatePicker();
    }

    private void setupCancelButton() {
        cancelBtn.setOnClickListener(btn -> switchScreen());
    }

    private void setupSaveButton() {
        saveBtn.setOnClickListener(v -> saveTrip());
    }

    private void setupDatePicker() {
        DatePickerHandler datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());
        datePickerHandler.setDateSelectedListener(date -> {
            if (date != null && !date.isEmpty()) {
                clearAll();
                currentTrip.setStartDate(date);
                loadTripDataFromDate(date);
                loadEventsForSelectedDate(date);
            }
        });
    }


    private void loadTripDataFromDate(String date) {
        // Fetch the trip data for the given date
        Trip trip = dataManager.getTripForDate(date);
        Log.d("AddTripActivity", "Loaded trip: " + trip);

        if (trip != null) {
            // Update the UI elements with the fetched trip details
            titleTrip.setText(trip.getTitle());
            countryTrip.setText(trip.getLocation());
            currentTrip = trip;  // Update currentTrip with the fetched trip data
        } else {
            // Clear the UI elements and create a new Trip if no trip is found
            titleTrip.setText("");
            countryTrip.setText("");
            currentTrip = new Trip();
            currentTrip.setStartDate(date);  // Set the date to the new Trip
        }
    }



    private void loadEventsForSelectedDate(String date) {
        Log.d("DATA",dataManager.getTrips().toString());
        ArrayList<EventTrip> events = dataManager.getEventsForDate(date);
        Log.d("AddTripActivity", "Fetched events: " + events);
        if (events != null) {
            eventList.clear();
            eventList.addAll(events);
        } else {
            eventList.clear();
            loadEventTrips();  // Optionally load default events
        }
        eventAdapter.notifyDataSetChanged();
    }




    private void saveTrip() {
        if (titleTrip.getText().toString().trim().isEmpty() || countryTrip.getText().toString().trim().isEmpty()) {
            Log.d("AddTripActivity", "Trip title or location cannot be empty");
            return;
        }

        Log.d("AddTripActivity", "Event List: " + eventAdapter.getEventList().toString());
        captureTripData();
        captureEventData();
        dataManager.addTrip(currentTrip);  // Add or update the trip in DataManager
        Log.d("AddTripActivity", "Trip saved: " + dataManager.toString());
        // switchScreen();
    }


    private void clearAll() {
        titleTrip.setText("");
        countryTrip.setText("");
        eventList.clear();
        loadEventTrips();  // Optionally reload default events
        currentTrip = new Trip();  // Create a new Trip instance
    }



    private void captureTripData() {
        currentTrip.setTitle(titleTrip.getText().toString());
        currentTrip.setLocation(countryTrip.getText().toString());
    }

    private void captureEventData() {
        ArrayList<EventTrip> eventTrips = eventAdapter.getEventList();
        HashMap<String, ArrayList<EventTrip>> eventMap = new HashMap<>();
        for (EventTrip eventTrip : eventTrips) {
            Log.d("AddTripActivity", "event -> " + eventTrip);
            String eventType = eventTrip.getEventType();
            if (!eventMap.containsKey(eventType)) {
                eventMap.put(eventType, new ArrayList<>());
            }
            Objects.requireNonNull(eventMap.get(eventType)).add(eventTrip);
        }
        currentTrip.setEventTrips(eventMap);
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        eventAdapter = new EventTripAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadEventTrips() {
        eventList.add(new EventTrip("", "", ""));
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}