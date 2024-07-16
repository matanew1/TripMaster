package com.example.tripmaster.Activity.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tripmaster.Activity.AddTripActivity;
import com.example.tripmaster.Activity.HomeActivity;
import com.example.tripmaster.Activity.ScreenSwitchListener;
import com.example.tripmaster.R;

public class MenuFragment extends Fragment implements ScreenSwitchListener {
    private Class<?> target;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.home_icon).setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Home clicked", Toast.LENGTH_SHORT).show();
            // Optionally set target here if you want to switch screens
            target = HomeActivity.class; // Replace with actual target class
            switchScreen();
        });
        view.findViewById(R.id.add_icon).setOnClickListener(v -> {
            // Handle add click
            target = AddTripActivity.class; // Replace with actual target class
            switchScreen();
        });
        view.findViewById(R.id.profile_icon).setOnClickListener(v -> {
            // Handle profile click
//            target = ProfileActivity.class; // Replace with actual target class
            switchScreen();
        });
    }

    @Override
    public void switchScreen() {
        if (target != null) {
            Intent intent = new Intent(requireActivity(), target);
            startActivity(intent);
        } else {
            Toast.makeText(requireActivity(), "Target not set", Toast.LENGTH_SHORT).show();
        }
    }
}
