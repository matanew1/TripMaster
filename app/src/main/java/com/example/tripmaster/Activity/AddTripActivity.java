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
    private Button cancelBtn, saveBtn, finishTripBtn, addEventBtn;
    private EditText titleTrip, countryTrip;
    private Trip currentTrip;

    private String currentEventDate;

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
        currentEventDate = "";
        recyclerView = findViewById(R.id.event_trips);
        etDate = findViewById(R.id.etDate);
        titleTrip = findViewById(R.id.title_trip);
        countryTrip = findViewById(R.id.country_trip);
        cancelBtn = findViewById(R.id.cancel_button);
        saveBtn = findViewById(R.id.save_btn);
        finishTripBtn = findViewById(R.id.finish_trip_btn);
        addEventBtn = findViewById(R.id.add_event_btn);

        // buttons
        setupCancelButton();
        setupSaveButton();
        setupFinishButton();
        setupAddEventButton();

        // date picker
        setupDatePicker();
    }

    private void setupAddEventButton() {
        addEventBtn.setOnClickListener(btn -> addNewEvent());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addNewEvent() {
        eventList.add(new EventTrip());
        eventAdapter.notifyDataSetChanged();
    }

    private void setupFinishButton() {
        finishTripBtn.setOnClickListener(btn -> {
            dataManager.addTrip(currentTrip);  // Add or update the trip in DataManager
            // switchScreen();
        });
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
                if (Objects.equals(currentTrip.getStartDate(), ""))
                    currentTrip.setStartDate(date);
                currentEventDate = date;
                loadEventsForSelectedDate(date);
            }
        });
    }

    private void loadEventsForSelectedDate(String date) {
        ArrayList<EventTrip> events = currentTrip.getEventTrips().get(date);
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
        captureTripData();
        captureEventData();
        System.out.println("SAVED DATA: " + currentTrip);
    }

    private void clearAll() {
        eventList.clear();
        eventAdapter.notifyDataSetChanged();
    }

    private void captureTripData() {
        currentTrip.setTitle(titleTrip.getText().toString());
        currentTrip.setLocation(countryTrip.getText().toString());
    }

    private void captureEventData() {

        // Create a new list for the current date to avoid reference issues
        ArrayList<EventTrip> eventsForDate = new ArrayList<>(eventList);

        currentTrip.getEventTrips().put(currentEventDate, eventsForDate);
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
