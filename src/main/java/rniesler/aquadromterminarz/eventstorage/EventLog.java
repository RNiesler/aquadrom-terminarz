package rniesler.aquadromterminarz.eventstorage;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@Builder
public class EventLog {
    private UUID aggregateId;
    private LocalDateTime eventDate;
    private String user;
    private String eventType;
    private String data;
    private long version;
}
