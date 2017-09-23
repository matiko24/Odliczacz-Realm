package com.matekome.odliczacz.data.realm;

import io.realm.RealmObject;

public class EventOccurrenceRealm extends RealmObject {
    private String date;
    private String description;

    public EventOccurrenceRealm() {}

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
