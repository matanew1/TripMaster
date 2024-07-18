package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

import androidx.annotation.Nullable;
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
        currentTrip = new Trip();

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
        saveBtn.setOnClickListener(v -> showConfirmationDialog());
    }

    private void setupDatePicker() {
        DatePickerHandler datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());
        datePickerHandler.setDateSelectedListener(currentTrip::setStartDate);
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Save Trip")
                .setMessage("Are you sure you want to save this trip?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> saveTrip())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void saveTrip() {
        captureTripData();
        captureEventData();
        dataManager.addTrip(currentTrip);
        System.out.println("RRRRRRRRES: " + dataManager.toString());
    }

    private void captureTripData() {
        currentTrip.setTitle(titleTrip.getText().toString());
        currentTrip.setLocation(countryTrip.getText().toString());
    }

    private void captureEventData() {
        ArrayList<EventTrip> eventTrips = eventAdapter.getEventList();
        HashMap<String, ArrayList<EventTrip>> eventMap = new HashMap<>();
        for (EventTrip eventTrip : eventTrips) {
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
