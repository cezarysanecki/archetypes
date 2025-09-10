package pl.cezarysanecki.partyarchetypeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipsFacade;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipFactory;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipRepository;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;

@Configuration
public class PartyRelationshipsConfig {

    @Bean
    PartyRelationshipsFacade partyRelationshipsFacade(
            PartiesFacade partiesFacade,
            PartyRelationshipRepository partyRelationshipRepository,
            EventPublisher eventPublisher
    ) {
        return new PartyRelationshipsFacade(
                partiesFacade,
                new PartyRelationshipFactory(() -> PartyRelationshipId::random),
                partyRelationshipRepository,
                eventPublisher
        );
    }
}

