package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRole;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.io.IOException;

class PartyRelationshipStd {

    static final Module PARTY_RELATIONSHOP_MODULE = new SimpleModule()
            .addSerializer(PartyRelationship.class, new PartyRelationshipStd.Serializer())
            .addDeserializer(PartyRelationship.class, new PartyRelationshipStd.Deserializer());

    private static final String ID = "id";
    private static final String FROM = "from";
    private static final String FROM_ROLE = "fromRole";
    private static final String TO = "to";
    private static final String TO_ROLE = "toRole";
    private static final String NAME = "name";

    static class Serializer extends StdSerializer<PartyRelationship> {
        public Serializer() {
            super(PartyRelationship.class);
        }

        @Override
        public void serialize(PartyRelationship value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField(ID, value.id().asString());
            gen.writeStringField(FROM, value.from().partyId().asString());
            gen.writeStringField(FROM_ROLE, value.from().role().name());
            gen.writeStringField(TO, value.to().partyId().asString());
            gen.writeStringField(TO_ROLE, value.to().role().name());
            gen.writeStringField(NAME, value.name().asString());
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
            var id = PartyRelationshipId.of(node.get(ID).asText());
            var fromPartyId = PartyId.of(node.get(FROM).asText());
            var fromRole = new Role(node.get(FROM_ROLE).asText());
            var toPartyId = PartyId.of(node.get(TO).asText());
            var toRole = new Role(node.get(TO_ROLE).asText());
            var name = new RelationshipName(node.get(NAME).asText());
            var from = new PartyRole(fromPartyId, fromRole);
            var to = new PartyRole(toPartyId, toRole);
            return new PartyRelationship(id, from, to, name);
        }
    }

}
