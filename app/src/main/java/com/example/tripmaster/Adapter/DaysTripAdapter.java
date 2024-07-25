package com.example.tripmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.R;

import java.util.ArrayList;

public class DaysTripAdapter extends RecyclerView.Adapter<DaysTripAdapter.DaysTripViewHolder> {

    private ArrayList<String> eventsDate;

    public DaysTripAdapter(ArrayList<String> eventsDate) {
        this.eventsDate = eventsDate;
    }

    @NonNull
    @Override
    public DaysTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_event_of_trip, parent, false);
        return new DaysTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysTripViewHolder holder, int position) {
        String eventDate = eventsDate.get(position);

        // Bind data to your views here
        holder.date.setText(eventDate);
    }

    @Override
    public int getItemCount() {
        return this.eventsDate.size();
    }

    public static class DaysTripViewHolder extends RecyclerView.ViewHolder {

        private Button date;

        public DaysTripViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.current_date_btn);
        }
    }
}
