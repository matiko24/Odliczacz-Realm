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

    public RealmResults<EventRealm> getAllEvents(boolean showPrivateEvents) {
        RealmResults<EventRealm> all = realm.where(EventRealm.class).equalTo("isPrivate", showPrivateEvents).findAll();
        return all;
    }

    public Event getEventById(int eventId) {
        EventMapper eventMapper = new EventMapper();
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", eventId).findFirst();
        return eventMapper.fromRealm(eventRealm);
    }

    public void insertNewEvent(Event event) {
        realm.beginTransaction();

        EventRealm newRealmEvent = realm.createObject(EventRealm.class, generateId());
        EventOccurrenceRealm newEventOccurrenceRealm = new EventOccurrenceRealm();

        newRealmEvent.setName(event.getName());
        newRealmEvent.setPrivate(event.isPrivate());
        newEventOccurrenceRealm.setDate(event.getEventOccurrences().get(0).getDate());
        newRealmEvent.getEventOccurrences().add(newEventOccurrenceRealm);

        realm.commitTransaction();
    }

    public void updateEvent(final Event event) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", event.getId()).findFirst();
                eventRealm.setName(event.getName());
                eventRealm.setPrivate(event.isPrivate());
            }
        });

    }

    public void deleteEventById(final int id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(EventRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    public RealmList<EventOccurrenceRealm> getEventOccurrencesByEventId(int id) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", id).findFirst();
        return eventRealm.getEventOccurrences();
    }

    public void addEventOccurrence(final Event event, final Date date) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", event.getId()).findFirst();

                EventOccurrenceRealm eventOccurrenceRealm = new EventOccurrenceRealm();
                eventOccurrenceRealm.setDate(date);
                RealmList<EventOccurrenceRealm> eventOccurrences = eventRealm.getEventOccurrences();
                int size = eventOccurrences.size();

                if (size == 0) {
                    eventOccurrences.add(eventOccurrenceRealm);
                } else if (size == 1) {
                    if (eventOccurrences.get(0).getDate().compareTo(date) < 0)
                        eventOccurrences.add(0, eventOccurrenceRealm);
                } else if (size > 1) {
                    if (eventOccurrences.first().getDate().compareTo(date) < 0)
                        eventOccurrences.add(0, eventOccurrenceRealm);
                    else if (eventOccurrences.last().getDate().compareTo(date) > 0)
                        eventOccurrences.add(size, eventOccurrenceRealm);
                    else {
                        int i = 0;
                        while (i < size - 1) {
                            Date d1 = eventOccurrences.get(i).getDate();
                            Date d2 = eventOccurrences.get(i + 1).getDate();
                            if (d1.compareTo(date) > 0 && d2.compareTo(date) < 0) {
                                eventOccurrences.add(i + 1, eventOccurrenceRealm);
                                break;
                            } else
                                i++;
                        }
                    }
                }

            }
        });
    }

    public void udpateEventOccurence(EventOccurrence eventOccurrence) {
        EventOccurrenceRealm eventOccurrenceRealm = realm.where(EventOccurrenceRealm.class).equalTo("date", eventOccurrence.getDate()).findFirst();

        realm.beginTransaction();
        eventOccurrenceRealm.setDate(eventOccurrence.getDate());
        eventOccurrenceRealm.setDescription(eventOccurrence.getDescription());
        realm.commitTransaction();
    }

    public boolean isSuchNameEventExist(String eventName) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("name", eventName).findFirst();
        return (eventRealm != null);
    }

    private long generateId() {
        Number maxValue = realm.where(EventRealm.class).max("id");
        long id = (maxValue != null) ? maxValue.longValue() + 1 : 0;
        return id;
    }

}
