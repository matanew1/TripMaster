package com.example.tripmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripmaster.Adapter.EventTripAdapter;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.R;
import com.example.tripmaster.Utils.DatePickerHandler;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;


public class AddTripActivity extends AppCompatActivity implements ScreenSwitchListener{

    private RecyclerView recyclerView;
    private ArrayList<EventTrip> eventList;
    private EventTripAdapter eventAdapter;
    private TextInputEditText etDate;
    private DatePickerHandler datePickerHandler;

    private EditText titleTrip, countryTrip;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initializeViews();
        setupRecyclerView();
        loadEventTrips();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.event_trips);
        etDate = findViewById(R.id.etDate);
        titleTrip = findViewById(R.id.title_trip);
        countryTrip = findViewById(R.id.country_trip);
        datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> switchScreen());
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        eventAdapter = new EventTripAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

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
