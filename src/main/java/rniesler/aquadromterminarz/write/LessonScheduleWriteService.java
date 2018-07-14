package rniesler.aquadromterminarz.write;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.commands.Command;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.handlers.CancelLessonScheduleCommandHandler;
import rniesler.aquadromterminarz.write.commands.handlers.NewLessonScheduleCommandHandler;

import java.util.UUID;

@Service
public class LessonScheduleWriteService {
    private final EventStore eventStore;
    private final NewLessonScheduleCommandHandler newLessonScheduleCommandHandler;
    private final CancelLessonScheduleCommandHandler cancelLessonScheduleCommandHandler;

    public LessonScheduleWriteService(EventStore eventStore, NewLessonScheduleCommandHandler newLessonScheduleCommandHandler, CancelLessonScheduleCommandHandler cancelLessonScheduleCommandHandler) {
        this.eventStore = eventStore;
        this.newLessonScheduleCommandHandler = newLessonScheduleCommandHandler;
        this.cancelLessonScheduleCommandHandler = cancelLessonScheduleCommandHandler;
    }

    public Mono<UUID> handleCommand(Command command) {
        if (command instanceof NewLessonScheduleCommand) {
            return newLessonScheduleCommandHandler.handle(eventStore, (NewLessonScheduleCommand) command);
        } else if (command instanceof CancelLessonScheduleCommand) {
            CancelLessonScheduleCommand cancelLessonScheduleCommand = (CancelLessonScheduleCommand) command;
            return cancelLessonScheduleCommandHandler.handle(eventStore, cancelLessonScheduleCommand);
        } else {
            throw new IllegalArgumentException("Unknown command type.");
        }
    }
}
