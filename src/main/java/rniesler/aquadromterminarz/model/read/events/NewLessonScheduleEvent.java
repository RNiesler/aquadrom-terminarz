package rniesler.aquadromterminarz.model.read.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import rniesler.aquadromterminarz.eventstorage.CreateAggregateEvent;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.model.write.LessonSchedule;

import java.io.IOException;

public class NewLessonScheduleEvent implements CreateAggregateEvent {
    @Getter
    private LessonSchedule model;

    public NewLessonScheduleEvent(EventLog eventLog) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.model = objectMapper.readValue(eventLog.getData(), LessonSchedule.class);
        } catch (IOException e) {
            throw new RuntimeException(); //TODO
        }
        this.model.setAggregateId(eventLog.getAggregateId());
    }
}
