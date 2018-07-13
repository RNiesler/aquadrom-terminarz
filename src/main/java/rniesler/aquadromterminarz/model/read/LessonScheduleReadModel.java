package rniesler.aquadromterminarz.model.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonScheduleReadModel {
    @Id
    private UUID aggregateId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String repeatExpression;

    private String student;
    private String notes;
}
