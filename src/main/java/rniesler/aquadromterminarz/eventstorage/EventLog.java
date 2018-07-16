package rniesler.aquadromterminarz.eventstorage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
    private UUID aggregateId;
    private LocalDateTime eventDate;
    private String user;
    private String eventType;
    private String data;
    private long version;
}
