package com.matekome.odliczacz.data.db;

import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventOccurrence;
import com.matekome.odliczacz.data.realm.EventOccurrenceRealm;
import com.matekome.odliczacz.data.realm.EventRealm;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class EventDao {
    private Realm realm;

    public EventDao() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    public RealmResults<EventRealm> getAllEvents() {
        RealmResults<EventRealm> all = realm.where(EventRealm.class).findAll();
        return all;
    }

    public Event getEventById(int eventId) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", eventId).findFirst();
        EventMapper eventMapper = new EventMapper();
        return eventMapper.fromRealm(eventRealm);
    }

    private Event getEventByName(String name) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("name", name).findFirst();
        if (eventRealm != null)
            return new EventMapper().fromRealm(eventRealm);
        else
            return null;
    }

    //Todo: zwracać w metodzie czy dodało
    public void insertNewEvent(Event event) {
        realm.beginTransaction();

        EventRealm eventRealm = realm.createObject(EventRealm.class, generateId());
        eventRealm.setName(event.getName());

        EventOccurrenceRealm realmObject = new EventOccurrenceRealm();
        realmObject.setDate(event.getEventOccurrences().get(0).getDate());
        eventRealm.getEventOccurrences().add(realmObject);

        realm.commitTransaction();
    }

    public RealmList<EventOccurrenceRealm> getEventOccurrencesByEventId(int id) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", id).findFirst();
        return eventRealm.getEventOccurrences();
    }

    public void addEventOccurrence(Event event, Date date) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", event.getId()).findFirst();

        realm.beginTransaction();
        EventOccurrenceRealm eventOccurrenceRealm = new EventOccurrenceRealm();
        eventOccurrenceRealm.setDate(date);
        eventRealm.getEventOccurrences().add(eventOccurrenceRealm);
        realm.commitTransaction();
    }

    public void udpateEventOccurence(EventOccurrence eventOccurrence) {
        EventOccurrenceRealm eventOccurrenceRealm = realm.where(EventOccurrenceRealm.class).equalTo("date", eventOccurrence.getDate()).findFirst();

        realm.beginTransaction();
        eventOccurrenceRealm.setDate(eventOccurrence.getDate());
        eventOccurrenceRealm.setDescription(eventOccurrence.getDescription());
        realm.commitTransaction();
    }

    public boolean isSuchNameEventExist(String eventName) {
        if (getEventByName(eventName) == null)
            return false;
        else
            return true;
    }

    private long generateId() {
        Number maxValue = realm.where(EventRealm.class).max("id");
        long id = (maxValue != null) ? maxValue.longValue() + 1 : 0;
        return id;
    }

}
