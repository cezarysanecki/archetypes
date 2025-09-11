package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRole;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.io.IOException;

class PartyRelationshipStd {

    static class Serializer extends StdSerializer<PartyRelationship> {
        public Serializer() {
            super(PartyRelationship.class);
        }

        @Override
        public void serialize(PartyRelationship value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("id", value.id().asString());
            gen.writeStringField("fromPartyId", value.from().partyId().asString());
            gen.writeStringField("fromRole", value.from().role().name());
            gen.writeStringField("toPartyId", value.to().partyId().asString());
            gen.writeStringField("toRole", value.to().role().name());
            gen.writeStringField("name", value.name().asString());
            gen.writeEndObject();
        }
    }

    static class Deserializer extends StdDeserializer<PartyRelationship> {
        public Deserializer() {
            super(PartyRelationship.class);
        }

        @Override
        public PartyRelationship deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            var id = PartyRelationshipId.of(node.get("id").asText());
            var fromPartyId = PartyId.of(node.get("fromPartyId").asText());
            var fromRole = new Role(node.get("fromRole").asText());
            var toPartyId = PartyId.of(node.get("toPartyId").asText());
            var toRole = new Role(node.get("toRole").asText());
            var name = new RelationshipName(node.get("name").asText());
            var from = new PartyRole(fromPartyId, fromRole);
            var to = new PartyRole(toPartyId, toRole);
            return new PartyRelationship(id, from, to, name);
        }
    }

}
