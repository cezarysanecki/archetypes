package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRole;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartyRelationshipStdTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(PartyRelationshipStd.PARTY_RELATIONSHOP_MODULE);

    @Test
    void checkIfPartyRelationshipIsProperlySerializedAndDeserialized() {
        // given
        PartyRelationship relationship = new PartyRelationship(
                PartyRelationshipId.random(),
                new PartyRole(PartyId.random(), new Role("FRIEND")),
                new PartyRole(PartyId.random(), new Role("FRIEND")),
                new RelationshipName("FRIENDSHIP")
        );

        // when
        Map<String, Object> values = OBJECT_MAPPER.convertValue(relationship, Map.class);
        PartyRelationship result = OBJECT_MAPPER.convertValue(values, PartyRelationship.class);

        // then
        assertEquals(relationship.id(), result.id());
        assertEquals(relationship.from(), result.from());
        assertEquals(relationship.to(), result.to());
        assertEquals(relationship.name(), result.name());
    }
}

