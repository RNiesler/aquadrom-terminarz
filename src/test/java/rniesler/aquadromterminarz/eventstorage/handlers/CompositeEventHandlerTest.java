package rniesler.aquadromterminarz.eventstorage.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompositeEventHandlerTest {

    private static class TestEvent implements rniesler.aquadromterminarz.eventstorage.handlers.Event {
    }

    private static class TestEvent2 implements rniesler.aquadromterminarz.eventstorage.handlers.Event {
    }

    private static class TestEvent3 implements rniesler.aquadromterminarz.eventstorage.handlers.Event {
    }

    private CompositeEventHandler handler;
    @Mock
    private EventHandler testEventHandler1;
    @Mock
    private EventHandler testEventHandler2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new CompositeEventHandler(List.of(testEventHandler1, testEventHandler2));
        when(testEventHandler1.canHandle(TestEvent.class)).thenReturn(true);
        when(testEventHandler2.canHandle(TestEvent.class)).thenReturn(true);
        when(testEventHandler2.canHandle(TestEvent2.class)).thenReturn(true);
    }

    @Test
    void testCanHandle() {
        assertTrue(handler.canHandle(TestEvent.class));
        assertTrue(handler.canHandle(TestEvent2.class));
        assertFalse(handler.canHandle(TestEvent3.class));
    }

    @Test
    void testHandle() {
        TestEvent testEvent = new TestEvent();
        when(testEventHandler1.handle(testEvent)).thenReturn(Mono.empty());
        when(testEventHandler2.handle(testEvent)).thenReturn(Mono.empty());
        handler.handle(testEvent).block();
        verify(testEventHandler1).handle(testEvent);
        verify(testEventHandler2).handle(testEvent);
    }

    @Test
    void testHandleInvalid() {
        TestEvent3 testEvent = new TestEvent3();
        handler.handle(testEvent);
        verify(testEventHandler1,never()).handle(any(Event.class));
        verify(testEventHandler2,never()).handle(any(Event.class));
    }
}
