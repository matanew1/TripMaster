package com.example.tripmaster.Model;

import com.example.tripmaster.R;

public enum EventTypeEnum {

    EMPTY("<empty>"),
    FLIGHT("Flight"),
    HOTEL("Hotel"),
    RESTAURANT("Restaurant"),
    MUSEUM("Museum"),
    BAR("Bar");

    private final String label;

    EventTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static EventTypeEnum fromLabel(String label) {
        for (EventTypeEnum type : values()) {
            if (type.getLabel().equals(label)) {
                return type;
            }
        }
        return EMPTY; // Default value if not found
    }

    public int getImageResource() {
        switch (this) {
            case FLIGHT:
                return R.drawable.ic_flight;
            case HOTEL:
                return R.drawable.ic_hotel;
            case RESTAURANT:
                return R.drawable.ic_restaurant;
            case MUSEUM:
                return R.drawable.ic_museum;
            case BAR:
                return R.drawable.ic_bar;
            case EMPTY:
            default:
                return R.drawable.ic_default; // Default image for EMPTY or unknown types
        }
    }
}
