package rniesler.aquadromterminarz.eventstorage;

import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EventStoreTest {
    private EventStore eventStore;

    @Mock
    private EventLogHandler eventLogHandler;
    @Mock
    private EventLogRepository eventLogRepository;
    @Mock
    private AggregateRepository aggregateRepository;
    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        eventStore = new EventStore(eventLogRepository, eventLogHandler, aggregateRepository, reactiveMongoTemplate);
    }

    @Test
    void testNewAggregate() {
        String testAggregateType = "testtype";
        when(aggregateRepository.save(any(Aggregate.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(eventLogRepository.save(any(EventLog.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(eventLogHandler.handle(any(EventLog.class))).thenReturn(Mono.empty());
        UUID uuid = eventStore.newAggregate(testAggregateType, new EventLog()).block();
        ArgumentCaptor<Aggregate> aggregateArgumentCaptor = ArgumentCaptor.forClass(Aggregate.class);
        verify(aggregateRepository).save(aggregateArgumentCaptor.capture());
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventLogRepository).save(eventLogArgumentCaptor.capture());
        ArgumentCaptor<EventLog> handlerArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventLogHandler).handle(handlerArgumentCaptor.capture());
        assertEquals(uuid, aggregateArgumentCaptor.getValue().getAggregateId());
        assertEquals(uuid, eventLogArgumentCaptor.getValue().getAggregateId());
        assertEquals(uuid, handlerArgumentCaptor.getValue().getAggregateId());
        assertEquals(0, aggregateArgumentCaptor.getValue().getVersion());
        assertEquals(-1, eventLogArgumentCaptor.getValue().getVersion());
        assertEquals(-1, handlerArgumentCaptor.getValue().getVersion());
        assertEquals(testAggregateType, aggregateArgumentCaptor.getValue().getAggregateType());
    }

    @Test
    void dontSaveLogWhenAggregateSaveFailed() {
        when(aggregateRepository.save(any(Aggregate.class))).thenReturn(Mono.error(new RuntimeException()));
        Mono<UUID> result = eventStore.newAggregate("testtype", new EventLog());
        assertThrows(RuntimeException.class, () -> result.block());
        verify(eventLogRepository, never()).save(any(EventLog.class));
        verify(eventLogHandler, never()).handle(any(EventLog.class));
    }

    @Test
    void dontHandleEventWhenLogSaveFailed() {
        when(aggregateRepository.save(any(Aggregate.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(eventLogRepository.save(any(EventLog.class))).thenReturn(Mono.error(new RuntimeException()));
        Mono<UUID> result = eventStore.newAggregate("testtype", new EventLog());
        assertThrows(RuntimeException.class, () -> result.block());
        verify(eventLogHandler, never()).handle(any(EventLog.class));
    }


    private static class StaticUpdateResult extends UpdateResult {
        private final int updatedCount;

        StaticUpdateResult(int updatedCount) {
            this.updatedCount = updatedCount;
        }

        @Override
        public boolean wasAcknowledged() {
            return true;
        }

        @Override
        public long getMatchedCount() {
            return updatedCount;
        }

        @Override
        public boolean isModifiedCountAvailable() {
            return true;
        }

        @Override
        public long getModifiedCount() {
            return updatedCount;
        }

        @Override
        public BsonValue getUpsertedId() {
            return null;
        }
    }

    @Test
    void saveHappyPath() {
        when(reactiveMongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Aggregate.class)))
                .thenReturn(Mono.just(new StaticUpdateResult(1)));
        when(eventLogRepository.save(any(EventLog.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(eventLogHandler.handle(any(EventLog.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        UUID uuid = UUID.randomUUID();
        EventLog eventLog = EventLog.builder()
                .aggregateId(uuid)
                .version(1)
                .build();
        EventLog result = eventStore.saveEvent(eventLog).block();
        verify(eventLogRepository).save(any(EventLog.class));
        verify(eventLogHandler).handle(any(EventLog.class));
    }

    @Test
    void saveWithOptimisticLockingFailure() {
        when(reactiveMongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Aggregate.class)))
                .thenReturn(Mono.just(new StaticUpdateResult(0)));
        assertThrows(OptimisticLockingFailureException.class, () -> eventStore.saveEvent(EventLog.builder().aggregateId(UUID.randomUUID()).build()).block());
        verify(eventLogRepository, never()).save(any(EventLog.class));
        verify(eventLogHandler, never()).handle(any(EventLog.class));

    }
}
