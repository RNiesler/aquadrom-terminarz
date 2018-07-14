package rniesler.aquadromterminarz.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationInMinutesDeserializer extends StdDeserializer<Duration> {
    public DurationInMinutesDeserializer() {
        this(null);
    }

    public DurationInMinutesDeserializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public Duration deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return Duration.ofMinutes(parser.getNumberValue().longValue());
    }
}
