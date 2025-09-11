package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.Company;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationUnit;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.Person;
import pl.cezarysanecki.partyarchetypeapp.model.PersonalData;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartyStdTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(PartyStd.PARTY_MODULE);

    @Test
    void checkIfPersonPartyIsProperlySerializedAndDeserialized() {
        //given
        Person person = new Person(
                PartyId.random(),
                new PersonalData("John", "Doe"),
                Set.of(new Role("EMPLOYEE")),
                Set.of(new SampleRegisteredIdentifier("12345678901")),
                Version.initial()
        );

        //when
        Map<String, Object> values = OBJECT_MAPPER.convertValue(person, Map.class);
        Person result = (Person) OBJECT_MAPPER.convertValue(values, Party.class);

        //then
        assertEquals(person.partyId(), result.partyId());
        assertEquals(person.personalData(), result.personalData());
        assertEquals(person.roles(), result.roles());
        assertEquals(person.registeredIdentifiers(), result.registeredIdentifiers());
        assertEquals(person.version(), result.version());
    }

    @Test
    void checkIfCompanyPartyIsProperlySerializedAndDeserialized() {
        //given
        Company company = new Company(
                PartyId.random(),
                new OrganizationName("Pepsi Cola"),
                Set.of(new Role("PAYER")),
                Set.of(new SampleRegisteredIdentifier("12345678901")),
                Version.initial()
        );

        //when
        Map<String, Object> values = OBJECT_MAPPER.convertValue(company, Map.class);
        Company result = (Company) OBJECT_MAPPER.convertValue(values, Party.class);

        //then
        assertEquals(company.partyId(), result.partyId());
        assertEquals(company.organizationName(), result.organizationName());
        assertEquals(company.roles(), result.roles());
        assertEquals(company.registeredIdentifiers(), result.registeredIdentifiers());
        assertEquals(company.version(), result.version());
    }

    @Test
    void checkIfOrganizationUnitPartyIsProperlySerializedAndDeserialized() {
        //given
        OrganizationUnit organizationUnit = new OrganizationUnit(
                PartyId.random(),
                new OrganizationName("Pepsi Cola"),
                Set.of(new Role("PAYER")),
                Set.of(new SampleRegisteredIdentifier("12345678901")),
                Version.initial()
        );

        //when
        Map<String, Object> values = OBJECT_MAPPER.convertValue(organizationUnit, Map.class);
        OrganizationUnit result = (OrganizationUnit) OBJECT_MAPPER.convertValue(values, Party.class);

        //then
        assertEquals(organizationUnit.partyId(), result.partyId());
        assertEquals(organizationUnit.organizationName(), result.organizationName());
        assertEquals(organizationUnit.roles(), result.roles());
        assertEquals(organizationUnit.registeredIdentifiers(), result.registeredIdentifiers());
        assertEquals(organizationUnit.version(), result.version());
    }

    record SampleRegisteredIdentifier(String value) implements RegisteredIdentifier {

        private static final String TYPE = "SAMPLE_REGISTERED_IDENTIFIER";

        @Override
        public String getType() {
            return TYPE;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}