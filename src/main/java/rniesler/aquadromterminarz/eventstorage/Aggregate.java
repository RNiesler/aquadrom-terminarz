package rniesler.aquadromterminarz.eventstorage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@AllArgsConstructor
@Data
public class Aggregate {
    @Id
    private UUID aggregateId;
    private String aggregateType;
    private long version;
}
