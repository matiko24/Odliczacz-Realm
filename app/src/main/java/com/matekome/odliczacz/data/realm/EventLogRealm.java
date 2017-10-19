package com.matekome.odliczacz.data.realm;

import java.util.Date;

import io.realm.RealmObject;

public class EventLogRealm extends RealmObject {
    private Date date;
    private String description;

    public EventLogRealm() {
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
