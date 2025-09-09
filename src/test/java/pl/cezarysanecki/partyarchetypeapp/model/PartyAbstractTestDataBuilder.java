package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Version;

import java.util.HashSet;
import java.util.Set;

sealed abstract class PartyAbstractTestDataBuilder<T extends Party> permits CompanyTestDataBuilder, OrganizationUnitTestDataBuilder, PersonTestDataBuilder {

    PartyId partyId;
    Set<Role> roles = new HashSet<>();
    Set<RegisteredIdentifier> registeredIdentifiers = new HashSet<>();
    Version version = Version.initial();

    PartyAbstractTestDataBuilder<T> withRandomPartyId() {
        partyId = PartyId.random();
        return this;
    }

    PartyAbstractTestDataBuilder<T> with(Role role) {
        roles.add(role);
        return this;
    }

    PartyAbstractTestDataBuilder<T> withRoleSetOf(Set<Role> roleSet) {
        roles.addAll(roleSet);
        return this;
    }

    PartyAbstractTestDataBuilder<T> with(RegisteredIdentifier identifier) {
        registeredIdentifiers.add(identifier);
        return this;
    }

    PartyAbstractTestDataBuilder<T> withRegisteredIdentifierSetOf(Set<RegisteredIdentifier> identifierSet) {
        registeredIdentifiers.addAll(identifierSet);
        return this;
    }

    abstract T build();
}
