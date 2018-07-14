package rniesler.aquadromterminarz.write.commands;

import rniesler.aquadromterminarz.commands.Command;
import rniesler.aquadromterminarz.write.model.LessonSchedule;

public abstract class AbstractLessonScheduleCommand implements Command {
    @Override
    public String getAggregateType() {
        return LessonSchedule.class.getName();
    }
}
