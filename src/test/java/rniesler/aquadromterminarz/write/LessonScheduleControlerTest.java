package rniesler.aquadromterminarz.write;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import rniesler.aquadromterminarz.write.model.LessonSchedule;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonScheduleControlerTest {
    private WebTestClient client;

    @Mock
    private LessonScheduleWriteService lessonScheduleWriteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        client = WebTestClient.bindToController(new LessonScheduleController(lessonScheduleWriteService)).build();
    }

    @Test
    void testCancel() {
        UUID uuid = UUID.randomUUID();
        when(lessonScheduleWriteService.handleCommand(any(CancelLessonScheduleCommand.class))).thenReturn(Mono.just(uuid));
        client.delete()
                .uri("/lessons/" + uuid.toString() + "?version=0")
                .exchange()
                .expectStatus().isOk();
        verify(lessonScheduleWriteService).handleCommand(any(CancelLessonScheduleCommand.class));
    }

    @Test
    void testNew() {
        UUID uuid = UUID.randomUUID();
        LessonSchedule model = new LessonSchedule();
        when(lessonScheduleWriteService.handleCommand(any(NewLessonScheduleCommand.class))).thenReturn(Mono.just(uuid));
        client.post()
                .uri("/lessons")
                .body(Mono.just(model), LessonSchedule.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }
}
