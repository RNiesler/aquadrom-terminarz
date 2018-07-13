package rniesler.aquadromterminarz.eventstorage;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface EventLogRepository extends ReactiveMongoRepository<EventLog, UUID> {
}
