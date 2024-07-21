package com.example.tripmaster.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.R;

import java.util.ArrayList;

public class EventTripAdapter extends RecyclerView.Adapter<EventTripAdapter.EventTripViewHolder> {

    private ArrayList<EventTrip> eventList;

    public EventTripAdapter(ArrayList<EventTrip> eventList) {
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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public ArrayList<EventTrip> getEventList() {
        return eventList;
    }

    public class EventTripViewHolder extends RecyclerView.ViewHolder {
        private Spinner spinnerEventType;
        private EditText etEventDescription;
        private EditText etTime;
        private final String[] eventTypes = {"Flight", "Hotel", "Restaurant", "Museum", "Bar"};
        private boolean isUserInteracting = true; // Flag to handle updates

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

            // Setup listener for spinner
            spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && isUserInteracting) {
                        EventTrip event = eventList.get(getAdapterPosition());
                        String selectedEventType = eventTypes[position];
                        if (!selectedEventType.equals(event.getEventType())) {
                            event.setEventType(selectedEventType);
                            System.out.println("spinner " + event);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No action needed
                }
            });
        }

        public void bind(@NonNull EventTrip event) {
            // Avoid spinner listener triggering
            isUserInteracting = false;
            etEventDescription.setText(event.getEventDescription());
            etTime.setText(event.getEventTime());
            int position = getEventTypePosition(event.getEventType());
            spinnerEventType.setSelection(position, false); // Avoid triggering onItemSelected
            isUserInteracting = true;

            // Add listeners to update event data
            etEventDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    EventTrip event = eventList.get(getAdapterPosition());
                    event.setEventDescription(s.toString());
                    System.out.println("DESC " + event);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            etTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    EventTrip event = eventList.get(getAdapterPosition());
                    event.setEventTime(s.toString());
                    System.out.println("TIME " + event);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        private int getEventTypePosition(String eventType) {
            for (int i = 0; i < eventTypes.length; i++) {
                if (eventTypes[i].equals(eventType)) {
                    return i;
                }
            }
            return 0;  // Default to the first item if not found
        }
    }
}
