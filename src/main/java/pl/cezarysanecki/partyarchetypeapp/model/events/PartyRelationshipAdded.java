package pl.cezarysanecki.partyarchetypeapp.model.events;

import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;

public record PartyRelationshipAdded(String partyRelationshipId,
                                     String fromPartyId, String fromPartyRole,
                                     String toPartyId, String toPartyRole,
                                     String relationshipName) implements PartyRelatedEvent, PublishedEvent {

}