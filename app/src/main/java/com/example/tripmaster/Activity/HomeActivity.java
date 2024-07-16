package com.example.tripmaster.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripList = new ArrayList<>();
        tripList.add(new Trip(R.drawable.def_img, "Kungliga Slottet", "Stockholm, Sweden", new Date().toString()));

        tripAdapter = new TripAdapter(tripList);
        recyclerView.setAdapter(tripAdapter);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_container, new Menu())
                    .commit();
        }
    }
}