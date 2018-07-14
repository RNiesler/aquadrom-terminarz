package rniesler.aquadromterminarz.read.events.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.read.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModel;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModelRepository;
import rniesler.aquadromterminarz.write.model.LessonSchedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class NewLessonScheduleEventHandlerTest {
    private NewLessonScheduleEventHandler handler;

    @Mock
    private LessonScheduleReadModelRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new NewLessonScheduleEventHandler(repository);
    }

    @Test
    void testCanHandle() {
        assertTrue(handler.canHandle(NewLessonScheduleEvent.class));
        assertFalse(handler.canHandle(Event.class));
    }

    @Test
    void testHandleInvalid() {
        handler.handle(new Event() {
            @Override
            public String getEventType() {
                return "test";
            }
        })
                .doOnSuccessOrError((result, exception) -> assertTrue(exception instanceof IllegalStateException))
                .subscribe();
    }

    @Test
    void testHandle() {
        UUID uuid = UUID.randomUUID();
        LessonSchedule model = LessonSchedule.builder()
                .aggregateId(uuid)
                .scheduleStart(LocalDate.now())
                .scheduleEnd(LocalDate.now().plusMonths(1))
                .startTime(LocalTime.NOON)
                .duration(Duration.ofHours(1))
                .repeatExpression("*")
                .student("student")
                .notes("notes")
                .build();
        NewLessonScheduleEvent event = new NewLessonScheduleEvent(model);
        ArgumentCaptor<LessonScheduleReadModel> modelArgumentCaptor = ArgumentCaptor.forClass(LessonScheduleReadModel.class);
        when(repository.save(modelArgumentCaptor.capture())).thenReturn(Mono.empty());
        handler.handle(event)
                .subscribe();
        LessonScheduleReadModel readModel = modelArgumentCaptor.getValue();
        assertEquals(uuid, readModel.getAggregateId());
        assertEquals(model.getScheduleStart(), readModel.getScheduleStart());
        assertEquals(model.getScheduleEnd(), readModel.getScheduleEnd());
        assertEquals(model.getStartTime(), readModel.getStartTime());
        assertEquals(model.getDuration(), readModel.getDuration());
        assertEquals(model.getRepeatExpression(), readModel.getRepeatExpression());
        assertEquals(model.getStudent(), readModel.getStudent());
        assertEquals(model.getNotes(), readModel.getNotes());

    }
}
