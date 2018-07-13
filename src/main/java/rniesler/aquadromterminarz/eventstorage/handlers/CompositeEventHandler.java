package rniesler.aquadromterminarz.eventstorage.handlers;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class CompositeEventHandler implements EventHandler {
    private List<EventHandler> handlers;

    public CompositeEventHandler(List<EventHandler> handlers) {
        this.handlers = handlers;
    }


    @Override
    public Mono<Void> handle(Event event) {
        Class<? extends Event> eventType = event.getClass();
        return handlers.stream().filter(handler -> handler.canHandle(eventType))
                .map(handler -> handler.handle(event))
                // combine monos
                .reduce(Mono.empty(), (m1, m2) -> m1.then(m2))
                .then();
    }

    @Override
    public boolean canHandle(Class<? extends Event> eventClass) {
        return true;
    }
}
