package rniesler.aquadromterminarz.commands;

import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.eventstorage.EventStore;

import java.util.UUID;

public interface CommandHandler<T extends Command> {
    /**
     * Handle the command by creating EventLog based on the command data and passing it to the EventStore
     *
     * @param eventStore Reference to the Event Store
     * @param command    The command itself
     */
    Mono<UUID> handle(EventStore eventStore, T command);
}
