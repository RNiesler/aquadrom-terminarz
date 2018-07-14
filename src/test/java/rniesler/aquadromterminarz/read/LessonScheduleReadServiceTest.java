package rniesler.aquadromterminarz.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModelRepository;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

public class LessonScheduleReadServiceTest {
    private LessonScheduleReadService service;
    @Mock
    private LessonScheduleReadModelRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        service = new LessonScheduleReadService(repository);
    }

    @Test
    void testFindBetweenDates() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        service.findScheduledBetweenDates(start, end);
        verify(repository).findAllByScheduleStartAfterAndScheduleEndBefore(start, end);
    }
}
