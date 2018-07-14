package rniesler.aquadromterminarz.write.model.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CancelLessonScheduleCommand extends AbstractLessonScheduleCommand {
    private UUID aggregateId;
    private long version;
}
