package pl.cezarysanecki.partyarchetypeapp.model.events;

import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;

public sealed interface PartyRelatedSucceededEvent extends PartyRelatedEvent {

    record RoleAdded(String partyId, String name) implements PartyRelatedSucceededEvent, PublishedEvent {
    }

    record RoleAdditionSkipped(String partyId, String name, String reason) implements PartyRelatedSucceededEvent {

        private static final String DUPLICATION_REASON = "DUPLICATION";

        public static RoleAdditionSkipped dueToDuplicationFor(String partyId, String name) {
            return new RoleAdditionSkipped(partyId, name, DUPLICATION_REASON);
        }

    }

    record RoleRemoved(String partyId, String name) implements PartyRelatedSucceededEvent, PublishedEvent {
    }

    record RoleRemovalSkipped(String partyId, String name, String reason) implements PartyRelatedSucceededEvent {

        private static final String MISSING_ROLE_REASON = "MISSING_ROLE";

        public static RoleRemovalSkipped dueToMissingRoleFor(String partyId, String name) {
            return new RoleRemovalSkipped(partyId, name, MISSING_ROLE_REASON);
        }

    }

    record RegisteredIdentifierAdded(String partyId, String type,
                                     String value) implements PartyRelatedSucceededEvent, PublishedEvent {
    }

    record RegisteredIdentifierAdditionSkipped(String partyId, String type, String value,
                                               String reason) implements PartyRelatedSucceededEvent {

        private static final String DUPLICATION_REASON = "DUPLICATION";

        public static RegisteredIdentifierAdditionSkipped dueToDataDuplicationFor(String partyId, String type, String value) {
            return new RegisteredIdentifierAdditionSkipped(partyId, type, value, DUPLICATION_REASON);
        }

    }

    record RegisteredIdentifierRemoved(String partyId, String type,
                                       String value) implements PartyRelatedSucceededEvent, PublishedEvent {
    }

    record RegisteredIdentifierRemovalSkipped(String partyId, String type, String value,
                                              String reason) implements PartyRelatedSucceededEvent {

        private static final String MISSING_IDENTIFIER_REASON = "MISSING_IDENTIFIER";

        public static RegisteredIdentifierRemovalSkipped dueToMissingIdentifierFor(String partyId, String type, String value) {
            return new RegisteredIdentifierRemovalSkipped(partyId, type, value, MISSING_IDENTIFIER_REASON);
        }
    }

    record PartyRelationshipAdded(String partyRelationshipId,
                                  String fromPartyId, String fromPartyRole,
                                  String toPartyId, String toPartyRole,
                                  String relationshipName) implements PartyRelatedEvent, PublishedEvent {

    }

    record PartyRelationshipAdditionSkipped(String partyRelationshipId,
                                            String fromPartyId, String fromPartyRole,
                                            String toPartyId, String toPartyRole,
                                            String relationshipName,
                                            String reason) implements PartyRelatedSucceededEvent {

        private static final String DUPLICATION_REASON = "DUPLICATION";

        public static PartyRelationshipAdditionSkipped dueToDataDuplicationFor(String partyRelationshipId,
                                                                               String fromPartyId, String fromPartyRole,
                                                                               String toPartyId, String toPartyRole,
                                                                               String relationshipName) {
            return new PartyRelationshipAdditionSkipped(partyRelationshipId, fromPartyId, fromPartyRole, toPartyId, toPartyRole, relationshipName, DUPLICATION_REASON);
        }

    }


}