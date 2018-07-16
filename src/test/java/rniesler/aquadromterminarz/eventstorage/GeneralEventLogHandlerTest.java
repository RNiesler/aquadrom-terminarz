package rniesler.aquadromterminarz.eventstorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeneralEventLogHandlerTest {
    private GeneralEventLogHandler handler;

    @Mock
    private EventHandler rootEventHandler;

    private String testEventType = "type";

    private static class TestEvent implements Event {
    }

    private static class TestEvent2 implements Event {
        public TestEvent2(EventLog eventLog) {
        }
    }

    private static class TestEvent3 implements Event {
    }

    private static class TestEvent4 implements Event {
        public TestEvent4(EventLog eventLog) {
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        Map<String, Function<EventLog, Event>> mapping = Map.of(testEventType, e -> new TestEvent());
        when(rootEventHandler.canHandle(TestEvent.class)).thenReturn(true);
        when(rootEventHandler.canHandle(TestEvent2.class)).thenReturn(true);
        handler = new GeneralEventLogHandler(rootEventHandler, mapping);
    }

    @Test
    void testWithMapping() {
        EventLog eventLog = EventLog.builder()
                .eventType(testEventType)
                .aggregateId(UUID.randomUUID())
                .build();
        handler.handle(eventLog);
        verify(rootEventHandler).handle(any(TestEvent.class));
    }

    @Test
    void testWithReflection() {
        EventLog eventLog = EventLog.builder()
                .eventType(TestEvent2.class.getName())
                .aggregateId(UUID.randomUUID())
                .build();
        handler.handle(eventLog);
        verify(rootEventHandler).handle(any(TestEvent2.class));
    }

    @Test
    void testWithReflectionAndNoConstructor() {
        EventLog eventLog = EventLog.builder()
                .eventType(TestEvent3.class.getName())
                .aggregateId(UUID.randomUUID())
                .build();
        assertThrows(IllegalStateException.class, () -> handler.handle(eventLog));
    }

    @Test
    void testWithUnknownEventType() {
        EventLog eventLog = EventLog.builder()
                .eventType("foo")
                .aggregateId(UUID.randomUUID())
                .build();
        assertThrows(IllegalStateException.class, () -> handler.handle(eventLog));
    }

    @Test
    void testWithUnsupportedEventType() {
        EventLog eventLog = EventLog.builder()
                .eventType(TestEvent4.class.getName())
                .aggregateId(UUID.randomUUID())
                .build();
        when(rootEventHandler.canHandle(TestEvent4.class)).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> handler.handle(eventLog));
    }

}
