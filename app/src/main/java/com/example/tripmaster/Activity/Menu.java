package com.example.tripmaster.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tripmaster.R;

public class Menu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.home_icon).setOnClickListener(v -> {
            // Handle home click
            Toast.makeText(getActivity(), "Home clicked", Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.add_icon).setOnClickListener(v -> {
            // Handle add click
        });
        view.findViewById(R.id.profile_icon).setOnClickListener(v -> {
            // Handle profile click
        });
    }

}
