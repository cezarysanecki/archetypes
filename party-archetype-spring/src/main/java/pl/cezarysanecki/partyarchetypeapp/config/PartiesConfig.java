package pl.cezarysanecki.partyarchetypeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRepository;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRoleFactory;

@Configuration
public class PartiesConfig {

    private final PartyRepository partyRepository;
    private final EventPublisher eventPublisher;

    public PartiesConfig(PartyRepository partyRepository, EventPublisher eventPublisher) {
        this.partyRepository = partyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Bean
    PartiesFacade partiesFacade() {
        return new PartiesFacade(
                partyRepository,
                eventPublisher,
                new PartyRoleFactory(),
                PartyId::random
        );
    }

}
