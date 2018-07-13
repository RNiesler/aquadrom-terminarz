package rniesler.aquadromterminarz.eventstorage.handlers;

public interface Event {
    default String getEventType() {
        return this.getClass().getName();
    }
}
