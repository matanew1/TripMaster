package com.example.tripmaster.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.EventTypeEnum;
import com.example.tripmaster.R;

import java.util.ArrayList;

public class EventTripAdapter extends RecyclerView.Adapter<EventTripAdapter.EventTripViewHolder> {

    private ArrayList<EventTrip> eventList;
    private final String[] eventTypeLabels;
    private final OnDeleteClickListener onDeleteClickListener;

    public EventTripAdapter(ArrayList<EventTrip> eventList, OnDeleteClickListener onDeleteClickListener) {
        this.eventList = eventList;
        this.onDeleteClickListener = onDeleteClickListener;
        eventTypeLabels = new String[EventTypeEnum.values().length];
        for (int i = 0; i < EventTypeEnum.values().length; i++) {
            eventTypeLabels[i] = EventTypeEnum.values()[i].toString();
        }
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

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public class EventTripViewHolder extends RecyclerView.ViewHolder {
        private Spinner spinnerEventType;
        private EditText etEventDescription;
        private EditText etTime;
        private final EventTypeEnum[] eventTypes = EventTypeEnum.values();
        private boolean isUserInteracting = true;

        public EventTripViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerEventType = itemView.findViewById(R.id.spinnerEventType);
            etEventDescription = itemView.findViewById(R.id.etEventDescription);
            etTime = itemView.findViewById(R.id.etTime);
            ImageView deleteEvent = itemView.findViewById(R.id.deleteEvent);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_spinner_item,
                    eventTypeLabels
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(adapter);

            spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION && isUserInteracting) {
                        EventTrip event = eventList.get(getAdapterPosition());
                        EventTypeEnum selectedEventType = eventTypes[position];
                        if (selectedEventType != event.getEventType()) {
                            event.setEventType(selectedEventType);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            deleteEvent.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void bind(@NonNull EventTrip event) {
            isUserInteracting = false;
            etEventDescription.setText(event.getEventDescription());
            etTime.setText(event.getEventTime());

            EventTypeEnum eventType = event.getEventType();
            int position = eventType != null ? getEventTypePosition(eventType) : 0;
            spinnerEventType.setSelection(position, false);
            isUserInteracting = true;

            etEventDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    EventTrip event = eventList.get(getAdapterPosition());
                    event.setEventDescription(s.toString());
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
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        private int getEventTypePosition(EventTypeEnum eventType) {
            for (int i = 0; i < eventTypes.length; i++) {
                if (eventTypes[i] == eventType) {
                    return i;
                }
            }
            return 0;
        }
    }
}
