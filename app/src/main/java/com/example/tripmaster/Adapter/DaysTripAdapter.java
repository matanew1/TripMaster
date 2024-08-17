package com.example.tripmaster.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.R;

import java.util.ArrayList;

@SuppressWarnings("all")
@SuppressLint("NotifyDataSetChanged")
public class DaysTripAdapter extends RecyclerView.Adapter<DaysTripAdapter.ViewHolder> {

    private ArrayList<String> eventDates;
    private OnDateClickListener onDateClickListener;
    private Context context;
    private String selectedDate;

    public DaysTripAdapter(ArrayList<String> eventDates, OnDateClickListener listener) {
        this.eventDates = eventDates;
        this.onDateClickListener = listener;
        // Set the first date as selected by default
        if (!eventDates.isEmpty()) {
            selectedDate = eventDates.get(0);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_event_of_trip, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = eventDates.get(position);
        holder.dateButtonView.setText(date);

        // Highlight selected date
        if (date.equals(selectedDate)) {
            holder.dateButtonView.setBackgroundColor(ContextCompat.getColor(context, R.color.button_default));
        } else {
            holder.dateButtonView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.dateButtonView.setOnClickListener(v -> {
            selectedDate = date;
            notifyDataSetChanged(); // Refresh all items to update colors
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

