package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.model.OrganizationNameFixture.someOrganizationName;
import static pl.cezarysanecki.partyarchetypeapp.model.PersonalDataFixture.somePersonalData;
import static pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifierFixture.someIdentifierSetOfSize;
import static pl.cezarysanecki.partyarchetypeapp.model.RoleFixture.someRoleSetOfSize;

class PartyTestSupport {

    private final PartyRepository partyRepository;

    PartyTestSupport(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    Person thereIsSomePerson() {
        Person party = PartyFixture.somePerson()
                .with(somePersonalData())
                .withRandomPartyId()
                .withRoleSetOf(someRoleSetOfSize(5))
                .withRegisteredIdentifierSetOf(someIdentifierSetOfSize(5))
                .build();
        return (Person) thereIs(party);
    }

    Organization thereIsSomeOrganization() {
        Company party = PartyFixture.someCompany()
                .with(someOrganizationName())
                .withRandomPartyId()
                .withRoleSetOf(someRoleSetOfSize(5))
                .withRegisteredIdentifierSetOf(someIdentifierSetOfSize(5))
                .build();
        return (Organization) thereIs(party);
    }

    Party thereIs(Party party) {
        partyRepository.save(party);
        return party;
    }
}
