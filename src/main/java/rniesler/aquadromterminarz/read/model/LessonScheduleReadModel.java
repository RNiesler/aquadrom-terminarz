package rniesler.aquadromterminarz.read.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import rniesler.aquadromterminarz.configuration.DurationInMinutesDeserializer;
import rniesler.aquadromterminarz.configuration.DurationInMinutesSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonScheduleReadModel {
    @Id
    private UUID aggregateId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleEnd;
    @JsonFormat(pattern = "kk:mm")
    private LocalTime startTime;

    @JsonSerialize(using = DurationInMinutesSerializer.class)
    @JsonDeserialize(using = DurationInMinutesDeserializer.class)
    private Duration duration;
    private String repeatExpression;

    private String student;
    private String notes;
}
