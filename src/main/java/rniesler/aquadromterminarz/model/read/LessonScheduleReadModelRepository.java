package rniesler.aquadromterminarz.model.read;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface LessonScheduleReadModelRepository extends ReactiveMongoRepository<LessonScheduleReadModel, UUID> {
}
