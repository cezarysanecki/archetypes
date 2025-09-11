package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
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
            .registerModule(
                    new SimpleModule()
                            .addSerializer(Party.class, new PartyStd.Serializer())
                            .addDeserializer(Party.class, new PartyStd.Deserializer())
            );

    @Test
    void handlePersonParty() {
        //given
        Person person = new Person(
                PartyId.random(),
                new PersonalData("John", "Doe"),
                Set.of(new Role("EMPLOYEE")),
                Set.of(new SampleRegisteredIdentifier("PERSONAL_IDENTIFICATION_NUMBER", "12345678901")),
                Version.initial()
        );

        //when
        Map<String, Object> values = OBJECT_MAPPER.convertValue(person, Map.class);
        Party result = OBJECT_MAPPER.convertValue(values, Party.class);

        assertEquals(person, result);
    }

    record SampleRegisteredIdentifier(String type, String value) implements RegisteredIdentifier {

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}