package rniesler.aquadromterminarz.write;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.write.model.LessonSchedule;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;

import java.util.UUID;

@RestController
@RequestMapping("/lessons")
public class LessonScheduleController {
    private final LessonScheduleWriteService lessonScheduleWriteService;

    public LessonScheduleController(LessonScheduleWriteService lessonScheduleWriteService) {
        this.lessonScheduleWriteService = lessonScheduleWriteService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<?>> createLessonSchedule(@RequestBody LessonSchedule model) {
        Mono<UUID> uuidMono = lessonScheduleWriteService.handleCommand(new NewLessonScheduleCommand(model));

        return uuidMono
                .map(uuid ->
                        ResponseEntity
                                .created(UriComponentsBuilder.newInstance().pathSegment("lesson", uuid.toString()).build().toUri())
                                .build());
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<UUID>> cancelLessonSchedule(@PathVariable("uuid") UUID uuid, @RequestParam("version") long version) {
        CancelLessonScheduleCommand command = new CancelLessonScheduleCommand(uuid, version);
        return lessonScheduleWriteService.handleCommand(command)
                .map(ignore -> ResponseEntity.ok().build());
    }
}
