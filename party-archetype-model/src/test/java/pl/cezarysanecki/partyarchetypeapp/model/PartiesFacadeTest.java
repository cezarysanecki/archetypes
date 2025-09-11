package pl.cezarysanecki.partyarchetypeapp.model;

import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher.InMemoryEventsPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.CompanyRegistered;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.OrganizationUnitRegistered;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.PersonRegistered;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierAdded;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierRemoved;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleAdded;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleRemoved;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.cezarysanecki.partyarchetypeapp.common.CollectionFixture.copyAndAdd;
import static pl.cezarysanecki.partyarchetypeapp.model.OrganizationNameFixture.someOrganizationName;
import static pl.cezarysanecki.partyarchetypeapp.model.PartyFixture.somePerson;
import static pl.cezarysanecki.partyarchetypeapp.model.PersonalDataFixture.somePersonalData;
import static pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifierFixture.someRegisteredIdentifier;
import static pl.cezarysanecki.partyarchetypeapp.model.RoleFixture.someRole;
import static pl.cezarysanecki.partyarchetypeapp.model.RoleFixture.someRoleSetOfSize;
import static pl.cezarysanecki.partyarchetypeapp.model.RoleFixture.stringSetFrom;

class PartiesFacadeTest {

    private final InMemoryPartyRepository repository = new InMemoryPartyRepository();
    private final InMemoryEventsPublisher eventPublisher = new InMemoryEventsPublisher();
    private final PartyRoleFactory partyRoleFactory = new PartyRoleFactory();
    private final FixablePartyIdSupplier partyIdSupplier = new FixablePartyIdSupplier();

    private final PartiesQueries partiesQueries = new PartiesQueries(repository);
    private final PartyTestSupport testSupport = new PartyTestSupport(repository);
    private final PartiesTestEventListener testEventListener = new PartiesTestEventListener(eventPublisher);

    private final PartiesFacade sut = new PartiesFacade(
            repository,
            eventPublisher,
            partyRoleFactory,
            partyIdSupplier
    );

    @Test
    void canRegisterPerson() {
        //given
        PersonalData personalData = somePersonalData();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();

        //when
        sut.registerPersonFor(personalData, roles, Set.of(identifier));

        //then
        Optional<Party> party = partiesQueries.findOneBy(identifier);

        assertTrue(party.isPresent());
        assertInstanceOf(Person.class, party.get());
        //and
        Person resultParty = (Person) party.get();
        assertEquals(personalData, resultParty.personalData());
        assertEquals(Set.of(identifier), resultParty.registeredIdentifiers());
        assertEquals(roles, resultParty.roles());
    }

    @Test
    void personRegisteredEventIsEmittedWhenOperationSucceeds() {
        //given
        PersonalData personalData = somePersonalData();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();
        //and
        PartyId partyId = PartyId.random();
        partyIdSupplier.fixPartyIdTo(partyId);

        //when
        sut.registerPersonFor(personalData, roles, Set.of(identifier));

        //then
        PersonRegistered expectedEvent = new PersonRegistered(partyId.asString(), personalData.firstName(), personalData.lastName(), Set.of(identifier.asString()), stringSetFrom(roles));
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));

        //cleanup
        partyIdSupplier.clear();
    }

    @Test
    void canRegisterCompany() {
        //given
        OrganizationName organizationName = someOrganizationName();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();

        //when
        sut.registerCompanyFor(organizationName, roles, Set.of(identifier));

        //then
        Optional<Party> party = partiesQueries.findOneBy(identifier);

        assertTrue(party.isPresent());
        assertInstanceOf(Company.class, party.get());
        //and
        Company resultParty = (Company) party.get();
        assertEquals(organizationName, resultParty.organizationName());
        assertEquals(Set.of(identifier), party.get().registeredIdentifiers());
        assertEquals(roles, party.get().roles());
    }

    @Test
    void companyRegisteredEventIsEmittedWhenOperationSucceeds() {
        //given
        OrganizationName organizationName = someOrganizationName();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();
        //and
        PartyId partyId = PartyId.random();
        partyIdSupplier.fixPartyIdTo(partyId);

        //when
        sut.registerCompanyFor(organizationName, roles, Set.of(identifier));

        //then
        CompanyRegistered expectedEvent = new CompanyRegistered(partyId.asString(), organizationName.asString(), Set.of(identifier.asString()), stringSetFrom(roles));
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));

        //cleanup
        partyIdSupplier.clear();
    }

    @Test
    void canRegisterOrganizationUnit() {
        //given
        OrganizationName organizationName = someOrganizationName();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();

        //when
        sut.registerOrganizationUnitFor(organizationName, roles, Set.of(identifier));

        //then
        Optional<Party> party = partiesQueries.findOneBy(identifier);

        assertTrue(party.isPresent());
        assertInstanceOf(OrganizationUnit.class, party.get());
        //and
        OrganizationUnit resultParty = (OrganizationUnit) party.get();
        assertEquals(organizationName, resultParty.organizationName());
        assertEquals(Set.of(identifier), party.get().registeredIdentifiers());
        assertEquals(roles, party.get().roles());
    }

    @Test
    void organizationUnitRegisteredEventIsEmittedWhenOperationSucceeds() {
        //given
        OrganizationName organizationName = someOrganizationName();
        Set<Role> roles = someRoleSetOfSize(5);
        RegisteredIdentifier identifier = someRegisteredIdentifier();
        //and
        PartyId partyId = PartyId.random();
        partyIdSupplier.fixPartyIdTo(partyId);

        //when
        sut.registerOrganizationUnitFor(organizationName, roles, Set.of(identifier));

        //then
        OrganizationUnitRegistered expectedEvent = new OrganizationUnitRegistered(partyId.asString(), organizationName.asString(), Set.of(identifier.asString()), stringSetFrom(roles));
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));

        //cleanup
        partyIdSupplier.clear();
    }

    @Test
    void canAddRoleToParty() {
        //given
        Party party = testSupport.thereIsSomePerson();
        Role newRole = someRole();

        //when
        sut.add(party.partyId(), newRole);

        //then
        Optional<Party> updatedParty = partiesQueries.findBy(party.partyId());

        assertEquals(copyAndAdd(party.roles(), newRole), updatedParty.get().roles());
    }

    @Test
    void roleAddedEventIsEmittedWhenOperationSucceeds() {
        //given
        Party party = testSupport.thereIsSomePerson();
        Role newRole = someRole();

        //when
        sut.add(party.partyId(), newRole);

        //then
        RoleAdded expectedEvent = new RoleAdded(party.partyId().asString(), newRole.asString());
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));
    }

    @Test
    void canRemoveRoleFromParty() {
        //given
        Role roleToBeRemoved = someRole();
        Party party = testSupport.thereIs(somePerson().withRandomPartyId().with(roleToBeRemoved).build());

        //when
        sut.remove(party.partyId(), roleToBeRemoved);

        //then
        Optional<Party> updatedParty = partiesQueries.findBy(party.partyId());

        assertTrue(updatedParty.get().roles().isEmpty());
    }

    @Test
    void roleRemovedEventIsEmittedWhenOperationSucceeds() {
        //given
        Role roleToBeRemoved = someRole();
        Party party = testSupport.thereIs(somePerson().withRandomPartyId().with(roleToBeRemoved).build());

        //when
        sut.remove(party.partyId(), roleToBeRemoved);

        //then
        RoleRemoved expectedEvent = new RoleRemoved(party.partyId().asString(), roleToBeRemoved.asString());
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));
    }

    @Test
    void canAddRegisteredIdentifierToParty() {
        //given
        Party party = testSupport.thereIsSomePerson();
        RegisteredIdentifier newRegisteredIdentifier = someRegisteredIdentifier();

        //when
        sut.add(party.partyId(), newRegisteredIdentifier);

        //then
        Optional<Party> updatedParty = partiesQueries.findBy(party.partyId());

        assertEquals(copyAndAdd(party.registeredIdentifiers(), newRegisteredIdentifier), updatedParty.get().registeredIdentifiers());
    }

    @Test
    void registeredIdentifierAddedEventIsEmittedWhenOperationSucceeds() {
        //given
        Party party = testSupport.thereIsSomePerson();
        RegisteredIdentifier newRegisteredIdentifier = someRegisteredIdentifier();

        //when
        sut.add(party.partyId(), newRegisteredIdentifier);

        //then
        RegisteredIdentifierAdded expectedEvent = new RegisteredIdentifierAdded(party.partyId().asString(), newRegisteredIdentifier.getType(), newRegisteredIdentifier.asString());
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));
    }

    @Test
    void canRemoveRegisteredIdentifierFromParty() {
        //given
        RegisteredIdentifier idToBeRemoved = someRegisteredIdentifier();
        Party party = testSupport.thereIs(somePerson().withRandomPartyId().with(idToBeRemoved).build());

        //when
        sut.remove(party.partyId(), idToBeRemoved);

        //then
        Optional<Party> updatedParty = partiesQueries.findBy(party.partyId());

        assertTrue(updatedParty.get().registeredIdentifiers().isEmpty());
    }

    @Test
    void registeredIdentifierRemovedEventIsEmittedWhenRegisteredIdentifierIsRemovedFromParty() {
        //given
        RegisteredIdentifier idToBeRemoved = someRegisteredIdentifier();
        Party party = testSupport.thereIs(somePerson().withRandomPartyId().with(idToBeRemoved).build());

        //when
        sut.remove(party.partyId(), idToBeRemoved);

        //then
        RegisteredIdentifierRemoved expectedEvent = new RegisteredIdentifierRemoved(party.partyId().asString(), idToBeRemoved.getType(), idToBeRemoved.asString());
        assertTrue(testEventListener.thereIsAnEventEqualTo(expectedEvent));
    }

}