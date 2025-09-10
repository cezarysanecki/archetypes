package pl.cezarysanecki.partyarchetypeapp.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;

import java.util.List;

@Component
public class JustForwardingEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public JustForwardingEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(PublishedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publish(List<PublishedEvent> events) {
        events.forEach(this::publish);
    }
}
