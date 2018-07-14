package rniesler.aquadromterminarz.write.commands.handlers;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.commands.CommandHandler;
import rniesler.aquadromterminarz.eventstorage.EventLog;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.read.events.CancelLessonScheduleEvent;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;

import java.util.UUID;

@Component
public class CancelLessonScheduleCommandHandler implements CommandHandler<CancelLessonScheduleCommand> {
    @Override
    public Mono<UUID> handle(EventStore eventStore, CancelLessonScheduleCommand command) {
        EventLog eventLog = EventLog.builder()
                .aggregateId(command.getAggregateId())
                .version(command.getVersion())
                .eventType(CancelLessonScheduleEvent.class.getName()) //dependency on read model
                .build();
        return eventStore.saveEvent(eventLog)
                .map(savedEventLog -> savedEventLog.getAggregateId());
    }
}
