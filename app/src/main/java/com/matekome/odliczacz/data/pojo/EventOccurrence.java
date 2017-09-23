package com.matekome.odliczacz.data.pojo;

public class EventOccurrence {
    private String date;
    private String description;

    public EventOccurrence(String date) {
        this.date = date;
        this.description = "";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
