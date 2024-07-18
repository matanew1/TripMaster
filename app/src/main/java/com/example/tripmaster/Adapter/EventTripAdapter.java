package com.example.tripmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.R;

import java.util.ArrayList;

public class EventTripAdapter extends RecyclerView.Adapter<EventTripAdapter.EventTripViewHolder> {

    private ArrayList<EventTrip> eventList; // Assuming Event is a model class for your events

    public EventTripAdapter(ArrayList<EventTrip> eventList) {
        super();
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_trip, parent, false);
        return new EventTripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTripViewHolder holder, int position) {
        EventTrip event = eventList.get(position);
        // Bind data to views in the ViewHolder
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventTripViewHolder extends RecyclerView.ViewHolder {
        private Spinner spinnerEventType;
        private EditText etEventDescription;
        private EditText etTime;
        private final String[] eventTypes = {"Flight", "Hotel", "Restaurant", "Museum","Bar"}; // Example event types


        public EventTripViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerEventType = itemView.findViewById(R.id.spinnerEventType);
            etEventDescription = itemView.findViewById(R.id.etEventDescription);
            etTime = itemView.findViewById(R.id.etTime);

            // Setup spinner adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, eventTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(adapter);
        }

        public void bind(@NonNull EventTrip event) {
            // Bind data to views
            spinnerEventType.setSelection(getEventTypePosition(event.getEventType()));
            etEventDescription.setText(event.getEventDescription());
            etTime.setText(event.getEventTime());

            // Set listeners or additional logic as needed
        }

        private int getEventTypePosition(String eventType) {
            for (int i = 0; i < eventTypes.length; i++) {
                if (eventTypes[i].equals(eventType)) {
                    return i;
                }
            }
            return 0; // Default to the first item if not found
        }
    }


}
