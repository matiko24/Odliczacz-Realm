package com.matekome.odliczacz.data.pojo;

import java.util.Date;

public class EventOccurrence {
    private Date date;
    private String description;

    public EventOccurrence(Date date) {
        this.date = date;
        this.description = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
