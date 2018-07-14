package rniesler.aquadromterminarz.read.events.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModelRepository;
import rniesler.aquadromterminarz.read.events.CancelLessonScheduleEvent;

@Slf4j
@Component
public class CancelLessonScheduleEventHandler implements EventHandler {
    private final LessonScheduleReadModelRepository repository;

    public CancelLessonScheduleEventHandler(LessonScheduleReadModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> handle(Event event) {
        if (event instanceof CancelLessonScheduleEvent) {
            return repository.deleteById(((CancelLessonScheduleEvent) event).getAggregateId());
        }
        return Mono.error(new IllegalStateException("Cannot handle the event of type: "+event.getClass().getName()));
    }

    @Override
    public boolean canHandle(Class<? extends Event> eventClass) {
        return CancelLessonScheduleEvent.class.isAssignableFrom(eventClass);
    }
}
