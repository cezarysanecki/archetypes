package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import static pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher.InMemoryEventsPublisher;

class PartiesTestEventListener implements InMemoryEventsPublisher.InMemoryEventObserver {

    private final BlockingQueue<PublishedEvent> events = new LinkedBlockingQueue<>();

    PartiesTestEventListener(InMemoryEventsPublisher eventsPublisher) {
        eventsPublisher.register(this);
    }

    @Override
    public void handle(PublishedEvent event) {
        events.add(event);
    }

    Optional<PublishedEvent> findMatching(Predicate<PublishedEvent> predicate) {
        return events.stream().filter(predicate).findFirst();
    }

    boolean thereIsAnEventEqualTo(PublishedEvent expectedEvent) {
        return findMatching(it -> it.equals(expectedEvent)).isPresent();
    }
}
