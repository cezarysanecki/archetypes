package pl.cezarysanecki.partyarchetypeapp.model.events;

public sealed interface PartyRelatedFailureEvent extends PartyRelatedEvent {

    record PartyNotFound(String partyId) implements PartyRelatedFailureEvent {
    }
    record PartyRegistrationFailed(Throwable reason) implements PartyRelatedFailureEvent {
        public static PartyRegistrationFailed dueTo(Throwable reason) {
            return new PartyRegistrationFailed(reason);
        }
    }

    record RoleAdditionFailed(String partyId, String role, String reason) implements PartyRelatedFailureEvent {
    }
    record RoleRemovalFailed(String partyId, String role, String reason) implements PartyRelatedFailureEvent {
    }

    record RegisteredIdentifierAdditionFailed(String partyId, String identifier, String reason) implements PartyRelatedFailureEvent {
    }
    record RegisteredIdentifierRemovalFailed(String partyId, String identifier, String reason) implements PartyRelatedFailureEvent {
    }

}