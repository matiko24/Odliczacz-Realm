package com.matekome.odliczacz.data.db;

import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventOccurrence;
import com.matekome.odliczacz.data.realm.EventOccurrenceRealm;
import com.matekome.odliczacz.data.realm.EventRealm;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public Event fromRealm(EventRealm eventRealm) {
        Event event = new Event();
        event.setId(eventRealm.getId());
        event.setName(eventRealm.getName());
        event.setPrivate(eventRealm.isPrivate());
        List<EventOccurrence> eventOccurrences = new ArrayList<>();
        for (EventOccurrenceRealm eventOccurrenceRealm : eventRealm.getEventOccurrences()) {
            EventOccurrence eventOccurrence = new EventOccurrence(eventOccurrenceRealm.getDate());
            eventOccurrence.setDescription(eventOccurrenceRealm.getDescription());
            eventOccurrences.add(eventOccurrence);
        }
        event.setEventOccurrences(eventOccurrences);
        return event;
    }
}
