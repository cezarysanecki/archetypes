package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Version;

import java.util.Set;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;

public abstract class Organization extends Party {

    private final OrganizationName organizationName;

    Organization(PartyId partyId,
                 OrganizationName organizationName,
                 Set<Role> roles,
                 Set<RegisteredIdentifier> registeredIdentifiers,
                 Version version) {
        super(partyId, roles, registeredIdentifiers, version);

        checkNotNull(organizationName, "Organization Name must not be null");

        this.organizationName = organizationName;
    }

    public OrganizationName getOrganizationName() {
        return this.organizationName;
    }
}
