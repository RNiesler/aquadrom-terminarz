package rniesler.aquadromterminarz.model.write.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rniesler.aquadromterminarz.model.write.LessonSchedule;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewLessonScheduleCommand extends AbstractLessonScheduleCommand {
    private LessonSchedule model;
}
