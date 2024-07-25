package com.example.tripmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.R;

import java.util.ArrayList;

public class DaysTripAdapter extends RecyclerView.Adapter<DaysTripAdapter.ViewHolder> {

    private ArrayList<String> eventDates;
    private OnDateClickListener onDateClickListener;

    public DaysTripAdapter(ArrayList<String> eventDates, OnDateClickListener listener) {
        this.eventDates = eventDates;
        this.onDateClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_event_of_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = eventDates.get(position);
        holder.dateButtonView.setText(date);
        holder.dateButtonView.setOnClickListener(v -> {
            if (onDateClickListener != null) {
                onDateClickListener.onDateClick(date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventDates.size();
    }

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button dateButtonView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateButtonView = itemView.findViewById(R.id.current_date_btn); // Ensure this ID matches your layout
        }
    }
}

