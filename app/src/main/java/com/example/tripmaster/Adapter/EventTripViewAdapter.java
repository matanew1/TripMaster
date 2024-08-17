package com.example.tripmaster.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.R;

import java.util.ArrayList;

@SuppressWarnings("all")
@SuppressLint("NotifyDataSetChanged")
public class EventTripViewAdapter extends RecyclerView.Adapter<EventTripViewAdapter.EventTripViewHolder> {

    private ArrayList<EventTrip> eventTrips;

    public EventTripViewAdapter(ArrayList<EventTrip> eventTrips) {
        this.eventTrips = eventTrips;
    }

    @NonNull
    @Override
    public EventTripViewAdapter.EventTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_trip, parent, false);
        return new EventTripViewAdapter.EventTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTripViewAdapter.EventTripViewHolder holder, int position) {
        EventTrip eventTrip = eventTrips.get(position);

        // Bind data to your views here
        holder.eventDate.setText(eventTrip.getEventTime());
        holder.eventDesc.setText(eventTrip.getEventDescription());
        holder.eventType.setText(eventTrip.getEventType().getLabel());
        holder.eventIcon.setImageResource(eventTrip.getEventType().getImageResource());
    }

    @Override
    public int getItemCount() {
        return eventTrips.size();
    }

    public void updateEvents(ArrayList<EventTrip> newEvents) {
        eventTrips.clear();
        eventTrips.addAll(newEvents);
        notifyDataSetChanged();
    }

    public static class EventTripViewHolder extends RecyclerView.ViewHolder {
        private ImageView eventIcon;
        private TextView eventType, eventDate, eventDesc;
        public EventTripViewHolder(@NonNull View itemView) {
            super(itemView);
            this.eventIcon = itemView.findViewById(R.id.event_type_icon);
            this.eventType = itemView.findViewById(R.id.event_type_view);
            this.eventDate = itemView.findViewById(R.id.event_date_view);
            this.eventDesc = itemView.findViewById(R.id.event_desc_view);
        }
    }
}
