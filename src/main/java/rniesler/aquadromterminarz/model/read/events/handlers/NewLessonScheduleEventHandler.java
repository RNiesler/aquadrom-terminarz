package rniesler.aquadromterminarz.model.read.events.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;
import rniesler.aquadromterminarz.model.read.LessonScheduleReadModel;
import rniesler.aquadromterminarz.model.read.LessonScheduleReadModelRepository;
import rniesler.aquadromterminarz.model.read.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.model.write.LessonSchedule;

@Slf4j
@Component
public class NewLessonScheduleEventHandler implements EventHandler {
    private final LessonScheduleReadModelRepository repository;

    public NewLessonScheduleEventHandler(LessonScheduleReadModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> handle(Event event) {
        if (event instanceof NewLessonScheduleEvent) {
            LessonSchedule modelFromEvent = ((NewLessonScheduleEvent) event).getModel();
            LessonScheduleReadModel readModel = LessonScheduleReadModel.builder()
                    .aggregateId(modelFromEvent.getAggregateId())
                    .startDate(modelFromEvent.getStartDate())
                    .endDate(modelFromEvent.getEndDate())
                    .repeatExpression(modelFromEvent.getRepeatExpression())
                    .student(modelFromEvent.getStudent())
                    .notes(modelFromEvent.getNotes())
                    .build();
            return repository.save(readModel).then();
        }
        return Mono.error(new IllegalStateException()); //TODO exception
    }

    @Override
    public boolean canHandle(Class<? extends Event> eventClass) {
        return NewLessonScheduleEvent.class.isAssignableFrom(eventClass);
    }
}
