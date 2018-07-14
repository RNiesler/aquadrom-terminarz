package rniesler.aquadromterminarz.write;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rniesler.aquadromterminarz.eventstorage.EventStore;
import rniesler.aquadromterminarz.write.commands.CancelLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.NewLessonScheduleCommand;
import rniesler.aquadromterminarz.write.commands.handlers.CancelLessonScheduleCommandHandler;
import rniesler.aquadromterminarz.write.commands.handlers.NewLessonScheduleCommandHandler;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LessonScheduleWriteServiceTest {
    private LessonScheduleWriteService service;
    @Mock
    private NewLessonScheduleCommandHandler newLessonScheduleCommandHandler;
    @Mock
    private CancelLessonScheduleCommandHandler cancelLessonScheduleCommandHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventStore eventStore = mock(EventStore.class);
        service = new LessonScheduleWriteService(eventStore, newLessonScheduleCommandHandler, cancelLessonScheduleCommandHandler);
    }

    @Test
    public void testHandleNewLessonCommand() {
        service.handleCommand(new NewLessonScheduleCommand());
        verify(newLessonScheduleCommandHandler).handle(any(EventStore.class), any(NewLessonScheduleCommand.class));
        verify(cancelLessonScheduleCommandHandler, never()).handle(any(), any());
    }

    @Test
    public void testHandleCancelCommand() {
        service.handleCommand(new CancelLessonScheduleCommand(UUID.randomUUID(), 0l));
        verify(cancelLessonScheduleCommandHandler).handle(any(EventStore.class), any(CancelLessonScheduleCommand.class));
        verify(newLessonScheduleCommandHandler, never()).handle(any(), any());
    }

    @Test
    public void testUnknownCommand() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.handleCommand(() -> null));
        verify(cancelLessonScheduleCommandHandler, never()).handle(any(), any());
        verify(newLessonScheduleCommandHandler, never()).handle(any(), any());
    }
}
