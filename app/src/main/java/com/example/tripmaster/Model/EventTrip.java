package com.example.tripmaster.Model;

public class EventTrip {
    private String eventType;
    private String eventDescription;
    private String eventTime;

    public EventTrip(String eventType, String eventDescription, String eventTime) {
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
