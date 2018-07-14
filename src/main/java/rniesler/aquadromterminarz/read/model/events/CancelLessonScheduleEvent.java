package rniesler.aquadromterminarz.read.model.events;

import lombok.Getter;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;

import java.util.UUID;

@Getter
public class CancelLessonScheduleEvent implements Event {
    private final UUID aggregateId;
    private final long aggregateVersion;

    public CancelLessonScheduleEvent(EventLog eventLog) {
        this.aggregateId = eventLog.getAggregateId();
        this.aggregateVersion = eventLog.getVersion();
    }

}
