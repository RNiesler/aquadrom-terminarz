package rniesler.aquadromterminarz.model.write;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonSchedule {
    private UUID aggregateId;
    private Long version;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String repeatExpression;

    private String student;
    private String notes;
}
