package rniesler.aquadromterminarz.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ScheduleReadControllerIntegrationTest {
    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @BeforeEach
    void setup() {
        client = WebTestClient.bindToApplicationContext(context).build();
        //TODO init test data
    }

    @Test
    void testWithDates() {
        String start = "2018-07-14";
        String end = "2018-07-15";
        client.get()
                .uri("/lessons?start={start}&end={end}", start, end)
                .exchange()
                .expectStatus().isOk();
        //TODO assert return values
    }
}
