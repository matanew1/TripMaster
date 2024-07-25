package com.example.tripmaster.Activity;

import android.content.Intent;
import android.os.Bundle;
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

public class TripViewActivity extends AppCompatActivity implements IScreenSwitch {

    private RecyclerView recyclerViewDays;
    private RecyclerView recyclerViewEvents;
    private Trip currentTrip;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data); // Ensure your layout file is named correctly

        // Initialize currentTrip (e.g., from Intent extras or a data source)
        if (getIntent() != null && getIntent().hasExtra("CLICKED_TRIP")) {
            currentTrip = (Trip) getIntent().getSerializableExtra("CLICKED_TRIP");
        } else {
            // Handle the case where currentTrip is null
            currentTrip = new Trip(); // Or show an error
        }

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> switchScreen());

        recyclerViewDays = findViewById(R.id.recyclerViewDays);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        // Set up RecyclerView for Days
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> eventDates = new ArrayList<>(currentTrip.getEventTrips().keySet());
        recyclerViewDays.setAdapter(new DaysTripAdapter(eventDates)); // Ensure DaysTripAdapter can handle ArrayList<String>

        // Set up RecyclerView for Events
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<EventTrip> events = currentTrip.getEventTrips().values().stream().
                flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        // use stream to set all values here
        recyclerViewEvents.setAdapter(new EventTripViewAdapter(events)); // Ensure EventTripViewAdapter can handle ArrayList<EventTrip>
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
