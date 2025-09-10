package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.UUID;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;

public record PartyId(UUID value) {

    public PartyId {
        checkArgument(value != null, "Party Id value cannot be null");
    }

    public static PartyId of(String value) {
        return of(UUID.fromString(value));
    }

    public static PartyId of(UUID value) {
        return new PartyId(value);
    }

    public static PartyId random() {
        return of(UUID.randomUUID());
    }

    public String asString() {
        return value.toString();
    }
}
