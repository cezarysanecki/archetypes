package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;

public record PartyRole(PartyId partyId, Role role) {

    public PartyRole {
        checkNotNull(partyId, "PartyId cannot be null");
        checkNotNull(role, "Role cannot be null");
    }

    static PartyRole of(PartyId partyId, String value) {
        return of(partyId, Role.of(value));
    }

    static PartyRole of(PartyId partyId, Role role) {
        return new PartyRole(partyId, role);
    }

}
