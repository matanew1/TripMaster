package com.example.tripmaster.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

public class EventTrip implements Serializable{

    private static final long serialVersionUID = 1L;
    private final String id;
    private EventTypeEnum eventType;
    private String eventDescription;
    private String eventTime;

    public EventTrip(EventTypeEnum eventType, String eventDescription, String eventTime) {
        this.id = String.valueOf(UUID.randomUUID());
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
    }

    public EventTrip() {
        this.id = "";
        this.eventType = EventTypeEnum.EMPTY;
        this.eventDescription = "";
        this.eventTime = "";
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
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
                "eventType='" + eventType.getLabel() + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventTime='" + eventTime + '\'' +
                '}';
    }

    public Object getId() {
        return this.id;
    }
}
