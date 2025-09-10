package pl.cezarysanecki.partyarchetypeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipFactory;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipRepository;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipsFacade;

@Configuration
public class PartyRelationshipsConfig {

    private final PartyRelationshipRepository partyRelationshipRepository;
    private final EventPublisher eventPublisher;

    public PartyRelationshipsConfig(PartyRelationshipRepository partyRelationshipRepository, EventPublisher eventPublisher) {
        this.partyRelationshipRepository = partyRelationshipRepository;
        this.eventPublisher = eventPublisher;
    }

    @Bean
    PartyRelationshipsFacade partyRelationshipsFacade(PartiesFacade partiesFacade) {
        return new PartyRelationshipsFacade(
                partiesFacade,
                new PartyRelationshipFactory(PartyRelationshipId::random),
                partyRelationshipRepository,
                eventPublisher
        );
    }
}

