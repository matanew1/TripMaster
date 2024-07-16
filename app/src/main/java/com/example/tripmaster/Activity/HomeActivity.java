package com.example.tripmaster.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initializeViews();
        setupRecyclerView();
        loadTrips();

        if (savedInstanceState == null) {
            loadMenuFragment();
        }
    }

    /**
     * Initializes the RecyclerView.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripList = new ArrayList<>();
        tripAdapter = new TripAdapter(tripList);
        recyclerView.setAdapter(tripAdapter);
    }

    /**
     * Loads trip data into the trip list.
     */
    private void loadTrips() {
        tripList.add(new Trip(R.drawable.def_img, "Kungliga Slottet", "Stockholm, Sweden", new Date().toString()));
        tripList.add(new Trip(R.drawable.def_img, "Kungliga Slottet", "Stockholm, Sweden", new Date().toString()));
        // You can add more trips here or load from a data source
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
}
