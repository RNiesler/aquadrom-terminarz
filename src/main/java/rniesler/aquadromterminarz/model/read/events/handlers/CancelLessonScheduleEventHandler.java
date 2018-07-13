package rniesler.aquadromterminarz.model.read.events.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;
import rniesler.aquadromterminarz.model.read.LessonScheduleReadModelRepository;
import rniesler.aquadromterminarz.model.read.events.CancelLessonScheduleEvent;

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
            return repository.deleteById(((CancelLessonScheduleEvent) event).getAggregateId())
                    .then();
        }
        return Mono.empty();
    }

    @Override
    public boolean canHandle(Class<? extends Event> eventClass) {
        return CancelLessonScheduleEvent.class.isAssignableFrom(eventClass);
    }
}
