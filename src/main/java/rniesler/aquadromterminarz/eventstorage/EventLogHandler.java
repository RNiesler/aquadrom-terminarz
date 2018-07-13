package rniesler.aquadromterminarz.eventstorage;

import reactor.core.publisher.Mono;

/**
 * Handle events based on EventLog entity.
 */
public interface EventLogHandler {
    Mono<Void> handle(EventLog event);
}
