package rniesler.aquadromterminarz.model.write.commands.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.commands.CommandExecutionException;
import rniesler.aquadromterminarz.commands.CommandHandler;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.model.read.events.NewLessonScheduleEvent;
import rniesler.aquadromterminarz.model.write.commands.NewLessonScheduleCommand;

import java.util.UUID;

@Component
public class NewLessonScheduleCommandHandler implements CommandHandler<NewLessonScheduleCommand> {
    @Override
    public Mono<UUID> handle(EventStore eventStore, NewLessonScheduleCommand command) {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        try {
            String data = jsonObjectMapper.writeValueAsString(command.getModel());
            EventLog eventLog = EventLog.builder()
                    .data(data)
                    .eventType(NewLessonScheduleEvent.class.getName()) //TODO avoid using read model in the write model
                    .build();
            return eventStore.newAggregate(command.getAggregateType(), eventLog);
        } catch (JsonProcessingException e) {
            throw new CommandExecutionException();
        }
    }
}
