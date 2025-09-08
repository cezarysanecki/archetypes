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

}