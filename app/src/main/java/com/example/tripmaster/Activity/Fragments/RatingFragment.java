package com.example.tripmaster.Activity.Fragments;// RatingFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.google.gson.Gson;

public class RatingFragment extends Fragment {

    private static final String ARG_TRIP = "trip";
    private TextView ratingText;

    private Trip trip;

    public static RatingFragment newInstance(Trip trip) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String tripJson = gson.toJson(trip);
        args.putString(ARG_TRIP, tripJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new Gson();
            String tripJson = getArguments().getString(ARG_TRIP);
            trip = gson.fromJson(tripJson, Trip.class);
        }

        ratingText = view.findViewById(R.id.average_rating);
        if (trip != null) {
            ratingText.setText(String.valueOf(trip.getAverageRating()));
        }
    }

    public void updateRating(float rating) {
        if (ratingText != null && trip != null) {
            ratingText.setText(String.valueOf(rating));
        }
    }
}
