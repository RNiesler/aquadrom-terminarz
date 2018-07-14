package rniesler.aquadromterminarz.write.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class CancelLessonScheduleCommand extends AbstractLessonScheduleCommand {
    private UUID aggregateId;
    private long version;
}
