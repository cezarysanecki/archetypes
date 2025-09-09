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

    record PartyRoleDefinitionFailed(String reason) implements PartyRelatedFailureEvent {

        private static final String POLICIES_NOT_MET_REASON = "Policies for assigning party role not met";

        public static PartyRoleDefinitionFailed dueToPoliciesNotMet() {
            return new PartyRoleDefinitionFailed(POLICIES_NOT_MET_REASON);
        }
    }

    record PartyRelationshipDefinitionFailed(String reason) implements PartyRelatedFailureEvent {

        private static final String POLICIES_NOT_MET_REASON = "Policies for defining party relationship not met";

        public static PartyRelationshipDefinitionFailed dueToPoliciesNotMet() {
            return new PartyRelationshipDefinitionFailed(POLICIES_NOT_MET_REASON);
        }

        public static PartyRelationshipDefinitionFailed dueTo(String reason) {
            return new PartyRelationshipDefinitionFailed(reason);
        }
    }

}