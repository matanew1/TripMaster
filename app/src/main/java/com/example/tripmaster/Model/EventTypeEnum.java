package com.example.tripmaster.Model;

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
}
