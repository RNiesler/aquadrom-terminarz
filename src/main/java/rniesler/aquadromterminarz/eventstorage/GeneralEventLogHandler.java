package rniesler.aquadromterminarz.eventstorage;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.handlers.Event;
import rniesler.aquadromterminarz.eventstorage.handlers.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

/**
 * Will try to instantiate Event class based on EventLog and then delegate handling of the event to a type safe handler.
 */
@Slf4j
public class GeneralEventLogHandler implements EventLogHandler {
    private Map<String, Function<EventLog, Event>> eventMapping;
    private final EventHandler eventHandler;

    public GeneralEventLogHandler(EventHandler eventHandler, Map<String, Function<EventLog, Event>> eventMapping) {
        this.eventMapping = eventMapping;
        this.eventHandler = eventHandler;
    }

    public Mono<Void> handle(final EventLog eventLog) {
        log.debug("Handle event for " + eventLog.getAggregateId());
        final String eventType = eventLog.getEventType();
        try {
            Event event;
            if (eventMapping.containsKey(eventType)) {
                event = eventMapping.get(eventType).apply(eventLog);
            } else {
                event = reflectionInstantiate(eventLog);
            }
            if (eventHandler.canHandle(event.getClass())) {
                return eventHandler.handle(event);
            } else {
                throw new IllegalStateException();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException | InstantiationException | IllegalStateException ex) {
            throw new IllegalStateException("No event type mapping found for the event type: " + eventType); //TODO proper exception
        }
    }

    protected Event reflectionInstantiate(final EventLog eventLog) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends Event> eventType = (Class<? extends Event>) Class.forName(eventLog.getEventType());
        return eventType.getConstructor(EventLog.class).newInstance(eventLog);
    }
}
