package com.example.tripmaster.Model;

import androidx.annotation.NonNull;

import java.util.UUID;

public class EventTrip {

    private String id;
    private String eventType;
    private String eventDescription;
    private String eventTime;

    public EventTrip(String eventType, String eventDescription, String eventTime) {
        this.id = String.valueOf(UUID.randomUUID());
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
    }

    public EventTrip() {
        this.id = "";
        this.eventType = "";
        this.eventDescription = "";
        this.eventTime = "";
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

    @NonNull
    @Override
    public String toString() {
        return "EventTrip{" +
                "eventType='" + eventType + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventTime='" + eventTime + '\'' +
                '}';
    }

    public Object getId() {
        return this.id;
    }
}
