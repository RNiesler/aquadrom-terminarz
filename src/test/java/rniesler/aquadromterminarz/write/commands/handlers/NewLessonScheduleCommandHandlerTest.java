package rniesler.aquadromterminarz.write.commands.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.write.model.LessonSchedule;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class NewLessonScheduleCommandHandlerTest {
    private NewLessonScheduleCommandHandler handler;

    @Mock
    private EventStore eventStore;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new NewLessonScheduleCommandHandler(objectMapper);
    }

    @Test
    void testHandle() throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        LessonSchedule model = LessonSchedule.builder()
                .aggregateId(uuid)
                .build();
        String serializedData = "testdata";
        NewLessonScheduleCommand command = new NewLessonScheduleCommand(model);
        when(objectMapper.writeValueAsString(model)).thenReturn(serializedData);
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        when(eventStore.newAggregate(eq(LessonSchedule.class.getName()), eventLogArgumentCaptor.capture()))
                .thenReturn(Mono.just(uuid));
        assertEquals(uuid, handler.handle(eventStore, command).block());
        assertEquals("rniesler.aquadromterminarz.read.events.NewLessonScheduleEvent", eventLogArgumentCaptor.getValue().getEventType());
    }
}
