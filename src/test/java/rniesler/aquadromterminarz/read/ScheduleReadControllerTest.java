package rniesler.aquadromterminarz.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class ScheduleReadControllerTest {
    private WebTestClient client;
    @Mock
    private LessonScheduleReadService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        client = WebTestClient.bindToController(new ScheduleReadController(service)).build();
    }

    /**
     * As custom converter is needed for LocalDate conversion, we can only test the default.
     */
    @Test
    void testWithDefaultDates() {
        client.get()
                .uri("/lessons")
                .exchange()
                .expectStatus().isOk();
        verify(service).findScheduledBetweenDates(eq(LocalDate.now()), eq(LocalDate.now().plusMonths(1)));
    }

}
