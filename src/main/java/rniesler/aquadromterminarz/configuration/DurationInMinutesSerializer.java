package rniesler.aquadromterminarz.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Duration;

public class DurationInMinutesSerializer extends StdSerializer<Duration> {
    public DurationInMinutesSerializer() {
        this(null);
    }

    public DurationInMinutesSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(
            Duration duration,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException {
        gen.writeNumber(duration.toMinutes());
    }
}
