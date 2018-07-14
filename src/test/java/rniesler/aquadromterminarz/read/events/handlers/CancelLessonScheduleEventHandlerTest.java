package rniesler.aquadromterminarz.read.events.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.read.events.CancelLessonScheduleEvent;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModelRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CancelLessonScheduleEventHandlerTest {
    private CancelLessonScheduleEventHandler handler;

    @Mock
    private LessonScheduleReadModelRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new CancelLessonScheduleEventHandler(repository);
    }

    @Test
    void testCanHandle() {
        assertTrue(handler.canHandle(CancelLessonScheduleEvent.class));
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
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testHandle() {
        UUID uuid = UUID.randomUUID();
        long version = 0l;
        CancelLessonScheduleEvent event = new CancelLessonScheduleEvent(EventLog.builder().aggregateId(uuid).version(version).build());
//        when(repository.deleteById(uuid)).thenReturn(Mono.empty());
        handler.handle(event);
        verify(repository).deleteById(uuid);
    }
}
