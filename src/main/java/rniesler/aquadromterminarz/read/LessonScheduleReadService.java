package rniesler.aquadromterminarz.read;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModel;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModelRepository;

import java.time.LocalDate;

@Service
public class LessonScheduleReadService {
    private final LessonScheduleReadModelRepository repository;

    public LessonScheduleReadService(LessonScheduleReadModelRepository repository) {
        this.repository = repository;
    }

    public Flux<LessonScheduleReadModel> findScheduledBetweenDates(LocalDate startDate, LocalDate endDate) {
        return repository.findAllByScheduleStartAfterAndScheduleEndBefore(startDate, endDate);
    }
}
