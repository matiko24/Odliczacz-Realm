package com.matekome.odliczacz.data.pojo;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private boolean isPrivate;
    private List<EventOccurrence> eventOccurrences;

    public Event() {
        eventOccurrences = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<EventOccurrence> getEventOccurrences() {
        return eventOccurrences;
    }

    public void setEventOccurrences(List<EventOccurrence> eventOccurrences) {
        this.eventOccurrences = eventOccurrences;
    }

    @Override
    public String toString() {
        return name;
    }
}
