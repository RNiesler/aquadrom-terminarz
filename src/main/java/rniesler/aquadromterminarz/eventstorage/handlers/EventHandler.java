package rniesler.aquadromterminarz.eventstorage.handlers;

import reactor.core.publisher.Mono;

public interface EventHandler {
    Mono<Void> handle(Event event);

    boolean canHandle(Class<? extends Event> eventClass);
}
