package rniesler.aquadromterminarz.eventstorage;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@Slf4j
public class EventStore {
    private final EventLogRepository eventLogRepository;
    private final EventLogHandler eventLogHandler;
    private final AggregateRepository aggregateRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public EventStore(EventLogRepository eventLogRepository, EventLogHandler eventLogHandler, AggregateRepository aggregateRepository, ReactiveMongoTemplate mongoTemplate) {
        this.eventLogRepository = eventLogRepository;
        this.eventLogHandler = eventLogHandler;
        this.aggregateRepository = aggregateRepository;
        this.mongoTemplate = mongoTemplate;
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
                }) //handle event on the model side
                .flatMap(savedEventLog -> eventLogHandler.handle(savedEventLog))
                .thenReturn(uuid);
    }

    public Mono<EventLog> saveEvent(EventLog eventLog) {
        // atomic query and increment
        Mono<UpdateResult> updateResultMono = mongoTemplate.updateFirst(
                query(where("aggregateId").is(eventLog.getAggregateId()).and("version").is(eventLog.getVersion())),
                new Update().inc("version", 1),
                Aggregate.class);
        return updateResultMono
                .map(updateResult -> {
                    if (updateResult.getModifiedCount() == 0) {
                        throw new OptimisticLockingFailureException("Aggregate optimistic locking failure");
                    }
                    return updateResult;
                })
                .flatMap(updateResult -> eventLogRepository.save(eventLog))
                .flatMap(savedEventLog -> eventLogHandler.handle(savedEventLog).thenReturn(savedEventLog));
    }
}
