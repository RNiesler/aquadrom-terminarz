package rniesler.aquadromterminarz.write.commands.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.commands.CommandExecutionException;
import rniesler.aquadromterminarz.commands.CommandHandler;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.read.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;

import java.util.UUID;

@Component
public class NewLessonScheduleCommandHandler implements CommandHandler<NewLessonScheduleCommand> {
    private final ObjectMapper objectMapper;

    public NewLessonScheduleCommandHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<UUID> handle(EventStore eventStore, NewLessonScheduleCommand command) {
        try {
            String data = objectMapper.writeValueAsString(command.getModel());
            EventLog eventLog = EventLog.builder()
                    .data(data)
                    .eventType(NewLessonScheduleEvent.class.getName()) //dependency on the read model
                    .build();
            return eventStore.newAggregate(command.getAggregateType(), eventLog);
        } catch (JsonProcessingException e) {
            throw new CommandExecutionException();
        }
    }
}
