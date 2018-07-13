package rniesler.aquadromterminarz.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventLogHandler;
import rniesler.aquadromterminarz.eventstorage.GeneralEventLogHandler;
import rniesler.aquadromterminarz.eventstorage.handlers.CompositeEventHandler;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;
import rniesler.aquadromterminarz.model.read.LessonScheduleReadModelRepository;
import rniesler.aquadromterminarz.model.read.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.model.read.events.handlers.CancelLessonScheduleEventHandler;
import rniesler.aquadromterminarz.model.read.events.handlers.NewLessonScheduleEventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class EventHandlingConfiguration {
    @Bean
    CompositeEventHandler eventHandler(LessonScheduleReadModelRepository lessonScheduleReadModelRepository) {
        return new CompositeEventHandler(List.of(
                new NewLessonScheduleEventHandler(lessonScheduleReadModelRepository),
                new CancelLessonScheduleEventHandler(lessonScheduleReadModelRepository)
        ));
    }

    @Bean
    EventLogHandler eventLogHandler(EventHandler eventHandler) {
        Map<String, Function<EventLog, Event>> eventMapping = new HashMap<>();
        eventMapping.put(NewLessonScheduleEvent.class.getName(), NewLessonScheduleEvent::new);
        return new GeneralEventLogHandler(eventHandler, eventMapping);
    }
}
