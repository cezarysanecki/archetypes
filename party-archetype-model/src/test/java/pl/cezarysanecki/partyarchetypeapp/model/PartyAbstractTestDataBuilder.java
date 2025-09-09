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

final class PersonTestDataBuilder extends PartyAbstractTestDataBuilder<Person> {

    PersonalData personalData = PersonalData.empty();

    PersonTestDataBuilder with(PersonalData personalData) {
        this.personalData = personalData;
        return this;
    }

    @Override
    Person build() {
        return new Person(partyId, personalData, roles, registeredIdentifiers, version);
    }
}

final class CompanyTestDataBuilder extends PartyAbstractTestDataBuilder<Company> {

    OrganizationName organizationName;

    CompanyTestDataBuilder with(OrganizationName organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    @Override
    Company build() {
        return new Company(partyId, organizationName, roles, registeredIdentifiers, version);
    }
}

final class OrganizationUnitTestDataBuilder extends PartyAbstractTestDataBuilder<OrganizationUnit> {

    OrganizationName organizationName;

    OrganizationUnitTestDataBuilder with(OrganizationName organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    @Override
    OrganizationUnit build() {
        return new OrganizationUnit(partyId, organizationName, roles, registeredIdentifiers, version);
    }
}