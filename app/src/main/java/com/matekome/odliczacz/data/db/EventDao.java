package com.matekome.odliczacz.data.db;

import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventLog;
import com.matekome.odliczacz.data.realm.EventLogRealm;
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
        EventLogRealm newEventLogRealm = new EventLogRealm();

        newRealmEvent.setName(event.getName());
        newRealmEvent.setPrivate(event.isPrivate());
        newEventLogRealm.setDate(event.getEventLogs().get(0).getDate());
        newRealmEvent.getEventLogs().add(newEventLogRealm);

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

    public RealmList<EventLogRealm> getEventLogsByEventId(int id) {
        EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", id).findFirst();
        return eventRealm.getEventLogs();
    }

    public void addEventLog(final Event event, final Date date) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EventRealm eventRealm = realm.where(EventRealm.class).equalTo("id", event.getId()).findFirst();

                EventLogRealm eventLogRealm = new EventLogRealm();
                eventLogRealm.setDate(date);
                RealmList<EventLogRealm> eventLogs = eventRealm.getEventLogs();
                int size = eventLogs.size();

                if (size == 0) {
                    eventLogs.add(eventLogRealm);
                } else if (size == 1) {
                    if (eventLogs.get(0).getDate().compareTo(date) < 0)
                        eventLogs.add(0, eventLogRealm);
                } else if (size > 1) {
                    if (eventLogs.first().getDate().compareTo(date) < 0)
                        eventLogs.add(0, eventLogRealm);
                    else if (eventLogs.last().getDate().compareTo(date) > 0)
                        eventLogs.add(size, eventLogRealm);
                    else {
                        int i = 0;
                        while (i < size - 1) {
                            Date d1 = eventLogs.get(i).getDate();
                            Date d2 = eventLogs.get(i + 1).getDate();
                            if (d1.compareTo(date) > 0 && d2.compareTo(date) < 0) {
                                eventLogs.add(i + 1, eventLogRealm);
                                break;
                            } else
                                i++;
                        }
                    }
                }

            }
        });
    }

    public void udpateEventOccurence(EventLog eventLog) {
        EventLogRealm eventLogRealm = realm.where(EventLogRealm.class).equalTo("date", eventLog.getDate()).findFirst();

        realm.beginTransaction();
        eventLogRealm.setDate(eventLog.getDate());
        eventLogRealm.setDescription(eventLog.getDescription());
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
