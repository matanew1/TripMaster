package com.example.tripmaster.Activity.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Adapter.TripAdapter;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter; // Create an adapter class for your RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your adapter with data
        tripAdapter = new TripAdapter(Collections.singletonList((Trip) getTripData())); // Implement your data fetching
        recyclerView.setAdapter(tripAdapter);

        return view;
    }

    private List<Trip> getTripData() {
        // Return a list of Trip objects (you'll need to define this class)
        return new ArrayList<>(); // Replace with actual data
    }
}
