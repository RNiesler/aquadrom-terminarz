package rniesler.aquadromterminarz.read.model;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

public interface LessonScheduleReadModelRepository extends ReactiveMongoRepository<LessonScheduleReadModel, UUID> {
    Flux<LessonScheduleReadModel> findAllByScheduleStartAfterAndScheduleEndBefore(LocalDate after, LocalDate before);
}
