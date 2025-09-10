package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.OrganizationUnitRegistered;

public final class OrganizationUnit extends Organization {

    public OrganizationUnit(PartyId partyId,
                            OrganizationName organizationName,
                            Set<Role> roles,
                            Set<RegisteredIdentifier> registeredIdentifiers,
                            Version version) {
        super(partyId, organizationName, roles, registeredIdentifiers, version);
    }

    @Override
    PartyRegistered toPartyRegisteredEvent() {
        return new OrganizationUnitRegistered(getPartyId().asString(), getOrganizationName().value(),
                getRegisteredIdentifiers().stream().map(RegisteredIdentifier::getValue).collect(toSet()),
                getRoles().stream().map(Role::asString).collect(toSet()));
    }
}