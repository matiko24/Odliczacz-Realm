package com.matekome.odliczacz.data.pojo;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private boolean isPrivate;
    private List<EventLog> eventLogs;

    public Event() {
        eventLogs = new ArrayList<>();
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

    public List<EventLog> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(List<EventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

}
