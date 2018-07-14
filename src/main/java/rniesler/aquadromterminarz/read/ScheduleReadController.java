package rniesler.aquadromterminarz.read;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import rniesler.aquadromterminarz.read.model.LessonScheduleReadModel;

import java.time.LocalDate;

@RestController
@RequestMapping("/lessons")
public class ScheduleReadController {
    private final LessonScheduleReadService lessonScheduleReadService;

    public ScheduleReadController(LessonScheduleReadService lessonScheduleReadService) {
        this.lessonScheduleReadService = lessonScheduleReadService;
    }

    @GetMapping({"", "/"})
    public Flux<LessonScheduleReadModel> getSchedulesInDates(
            @RequestParam(name = "start", required = false)
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "end", required = false)
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }
        return lessonScheduleReadService.findScheduledBetweenDates(startDate, endDate);
    }
}
