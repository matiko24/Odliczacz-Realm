package com.matekome.odliczacz.data.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private boolean isPrivate;
    private RealmList<EventLogRealm> eventLogs;

    public EventRealm() {
        eventLogs = new RealmList<>();
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

    public RealmList<EventLogRealm> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(RealmList<EventLogRealm> eventLogs) {
        this.eventLogs = eventLogs;
    }

}
