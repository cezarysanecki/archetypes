package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.UUID;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;

public record PartyRelationshipId(UUID value) {

    public PartyRelationshipId {
        checkNotNull(value, "Party relationship id value cannot be null");
    }

    public String asString() {
        return value.toString();
    }

    public static PartyRelationshipId of(UUID value) {
        return new PartyRelationshipId(value);
    }

    public static PartyRelationshipId random() {
        return of(UUID.randomUUID());
    }
}
