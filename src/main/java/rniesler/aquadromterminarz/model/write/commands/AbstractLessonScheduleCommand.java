package rniesler.aquadromterminarz.model.write.commands;

import rniesler.aquadromterminarz.commands.Command;
import rniesler.aquadromterminarz.model.write.LessonSchedule;

public abstract class AbstractLessonScheduleCommand implements Command {
    @Override
    public String getAggregateType() {
        return LessonSchedule.class.getName();
    }
}
