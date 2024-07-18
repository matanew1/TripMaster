package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Adapter.EventTripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Utils.DatePickerHandler;
import com.example.tripmaster.Utils.TextChangeHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity implements ScreenSwitchListener {

    private RecyclerView recyclerView;
    private ArrayList<EventTrip> eventList;
    private EventTripAdapter eventAdapter;
    private TextInputEditText etDate;
    private DatePickerHandler datePickerHandler;
    private DataManager dataManager;

    private Button cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        dataManager = DataManager.getInstance();

        initializeViews();
        setupRecyclerView();
        loadEventTrips();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.event_trips);

        dataManager.addTrip(new Trip());

        cancel_btn.setOnClickListener(btn -> switchScreen());

        etDate = findViewById(R.id.etDate);
        datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());

        datePickerHandler.setDateSelectedListener(date -> {
            Trip currentTrip = getCurrentTrip();
            if (currentTrip != null) {
                currentTrip.setStartDate(date);
                dataManager.updateTrip(currentTrip);
            }
        });

        TextChangeHandler textChangeHandler = new TextChangeHandler();
        textChangeHandler.setupTextWatchers(findViewById(R.id.title_trip), findViewById(R.id.country_trip));
    }

    private Trip getCurrentTrip() {
        if (dataManager.getTrips() != null && !dataManager.getTrips().isEmpty()) {
            return dataManager.getTrips().get(dataManager.getSize() - 1);
        }
        return null;
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        eventAdapter = new EventTripAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadEventTrips() {
        eventList = new ArrayList<>();
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
