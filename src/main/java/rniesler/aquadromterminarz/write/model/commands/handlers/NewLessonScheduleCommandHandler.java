package rniesler.aquadromterminarz.write.model.commands.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.commands.CommandExecutionException;
import rniesler.aquadromterminarz.commands.CommandHandler;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.read.model.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.write.model.commands.NewLessonScheduleCommand;

import java.time.Duration;
import java.util.UUID;

@Component
public class NewLessonScheduleCommandHandler implements CommandHandler<NewLessonScheduleCommand> {
    @Override
    public Mono<UUID> handle(EventStore eventStore, NewLessonScheduleCommand command) {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        jsonObjectMapper.registerModule(new JavaTimeModule());
        command.getModel().setDuration(Duration.ofHours(1)); //TODO remove
        try {
            String data = jsonObjectMapper.writeValueAsString(command.getModel());
            EventLog eventLog = EventLog.builder()
                    .data(data)
                    .eventType(NewLessonScheduleEvent.class.getName()) //TODO avoid using model model in the model model
                    .build();
            return eventStore.newAggregate(command.getAggregateType(), eventLog);
        } catch (JsonProcessingException e) {
            throw new CommandExecutionException();
        }
    }
}
