package rniesler.aquadromterminarz.read.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rniesler.aquadromterminarz.eventstorage.CreateAggregateEvent;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.write.model.LessonSchedule;

import java.io.IOException;

@AllArgsConstructor
public class NewLessonScheduleEvent implements CreateAggregateEvent {
    @Getter
    private LessonSchedule model;

    public NewLessonScheduleEvent(EventLog eventLog) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            this.model = objectMapper.readValue(eventLog.getData(), LessonSchedule.class);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
        this.model.setAggregateId(eventLog.getAggregateId());
    }

}
