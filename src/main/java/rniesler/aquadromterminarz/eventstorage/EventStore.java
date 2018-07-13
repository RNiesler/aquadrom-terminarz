package rniesler.aquadromterminarz.eventstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class EventStore { //TODO make reactive
    private final EventLogRepository eventLogRepository;
    private final EventLogHandler eventLogHandler;
    private final AggregateRepository aggregateRepository;

    public EventStore(EventLogRepository eventLogRepository, EventLogHandler eventLogHandler, AggregateRepository aggregateRepository) {
        this.eventLogRepository = eventLogRepository;
        this.eventLogHandler = eventLogHandler;
        this.aggregateRepository = aggregateRepository;
    }

    public Mono<UUID> newAggregate(String aggregateType, EventLog eventLog) {
        UUID uuid = UUID.randomUUID();
        Aggregate aggregate = new Aggregate(uuid, aggregateType, 0);
        return aggregateRepository.save(aggregate)
                .flatMap(ignore -> {
                    eventLog.setAggregateId(uuid);
                    eventLog.setVersion(-1);
                    eventLog.setUser("admin"); //TODO authentication
                    eventLog.setEventDate(LocalDateTime.now());
                    return eventLogRepository.save(eventLog);
                }) //handle event on the read side
                .flatMap(savedEventLog -> eventLogHandler.handle(savedEventLog))
                .thenReturn(uuid);
    }

    public Mono<EventLog> saveEvent(EventLog eventLog) {
        return aggregateRepository.findById(eventLog.getAggregateId()) // TODO make it atomic!!!!!
                .flatMap(aggregate -> {
                    if (aggregate.getVersion() != eventLog.getVersion()) {
                        throw new RuntimeException("Optimistic Locking failure"); //TODO exception
                    }
                    aggregate.setVersion(aggregate.getVersion() + 1);
                    return aggregateRepository.save(aggregate);
                })
                .flatMap(aggregate -> eventLogRepository.save(eventLog))
                .flatMap(savedEventLog -> eventLogHandler.handle(savedEventLog).thenReturn(savedEventLog));
    }
}
