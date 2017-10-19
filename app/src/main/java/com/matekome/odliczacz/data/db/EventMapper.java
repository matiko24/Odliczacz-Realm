package com.matekome.odliczacz.data.db;

import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventLog;
import com.matekome.odliczacz.data.realm.EventLogRealm;
import com.matekome.odliczacz.data.realm.EventRealm;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public Event fromRealm(EventRealm eventRealm) {
        Event event = new Event();
        List<EventLog> eventLogs = new ArrayList<>();

        event.setId(eventRealm.getId());
        event.setName(eventRealm.getName());
        event.setPrivate(eventRealm.isPrivate());

        for (EventLogRealm eventLogRealm : eventRealm.getEventLogs()) {
            EventLog eventLog = new EventLog(eventLogRealm.getDate());
            eventLog.setDescription(eventLogRealm.getDescription());
            eventLogs.add(eventLog);
        }
        event.setEventLogs(eventLogs);

        return event;
    }
}
