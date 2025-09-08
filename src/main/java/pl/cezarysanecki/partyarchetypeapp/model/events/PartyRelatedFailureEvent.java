package pl.cezarysanecki.partyarchetypeapp.model.events;

public sealed interface PartyRelatedFailureEvent extends PartyRelatedEvent {

    record RoleAdditionFailed(String partyId, String role, String reason) implements PartyRelatedFailureEvent {
    }
    record RoleRemovalFailed(String partyId, String role, String reason) implements PartyRelatedFailureEvent {
    }

}