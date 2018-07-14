package rniesler.aquadromterminarz.write.commands.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CancelLessonScheduleCommandHandlerTest {
    private CancelLessonScheduleCommandHandler handler;

    @Mock
    private EventStore eventStore;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new CancelLessonScheduleCommandHandler();
    }

    @Test
    void testHandle() {
        UUID uuid = UUID.randomUUID();
        long version = 8l;
        CancelLessonScheduleCommand command = CancelLessonScheduleCommand.builder()
                .aggregateId(uuid)
                .version(version)
                .build();
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        when(eventStore.saveEvent(eventLogArgumentCaptor.capture())).then(invocation -> Mono.just(invocation.getArgument(0)));
        assertEquals(uuid, handler.handle(eventStore, command).block());
        eventLogArgumentCaptor.getValue().getEventType().equals("rniesler.aquadromterminarz.read.events.CancelLessonScheduleEvent");
    }
}
