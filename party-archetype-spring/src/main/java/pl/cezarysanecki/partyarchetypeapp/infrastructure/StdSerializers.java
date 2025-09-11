package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.Company;
import pl.cezarysanecki.partyarchetypeapp.model.Organization;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationUnit;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.Person;
import pl.cezarysanecki.partyarchetypeapp.model.PersonalData;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.model.Role;
import pl.cezarysanecki.partyarchetypeapp.utils.PartyFactory;
import pl.cezarysanecki.partyarchetypeapp.utils.RegisteredIdentifiersFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class StdSerializers {

    private static final PartyFactory PARTY_FACTORY = new PartyFactory();
    private static final RegisteredIdentifiersFactory REGISTERED_IDENTIFIERS_FACTORY = new RegisteredIdentifiersFactory();

    static class PartySerializer extends StdSerializer<Party> {

        public PartySerializer() {
            super(Party.class);
        }

        @Override
        public void serialize(Party value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("id", value.partyId().asString());

            for (RegisteredIdentifier registeredIdentifier : value.registeredIdentifiers()) {
                gen.writeStringField("registeredIdentifiers." + registeredIdentifier.getType(), registeredIdentifier.getValue());
            }

            gen.writeArrayFieldStart("roles");
            for (Role role : value.roles()) {
                gen.writeString(role.name());
            }
            gen.writeEndArray();

            if (value instanceof Person person) {
                gen.writeStringField("firstName", person.personalData().firstName());
                gen.writeStringField("lastName", person.personalData().lastName());
            }
            if (value instanceof Organization organization) {
                gen.writeStringField("organizationName", organization.organizationName().value());
            }

            gen.writeStringField("type", value.getClass().getSimpleName());
            gen.writeNumberField("version", value.version().value());
            gen.writeEndObject();
        }
    }

    static class PartyDeserializer extends StdDeserializer<Party> {

        public PartyDeserializer() {
            super(Party.class);
        }

        @Override
        public Party deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode jsonNode = p.getCodec().readTree(p);

            String type = jsonNode.get("type").asText();
            PartyId partyId = PartyId.of(jsonNode.get("id").asText());
            Version version = Version.of(jsonNode.get("version").asLong());
            Set<Role> roles = StreamSupport.stream(jsonNode.get("roles").spliterator(), false)
                    .map(roleName -> new Role(roleName.asText()))
                    .collect(Collectors.toSet());
            Set<RegisteredIdentifier> registeredIdentifiers = jsonNode.properties().stream()
                    .filter(nodeEntry -> nodeEntry.getKey().startsWith("registeredIdentifiers."))
                    .map(nodeEntry -> REGISTERED_IDENTIFIERS_FACTORY.create(
                            nodeEntry.getKey().substring("registeredIdentifiers.".length()),
                            nodeEntry.getValue().asText()))
                    .collect(Collectors.toSet());

            Class<? extends Party> partySubclass = PARTY_FACTORY.findSubclassBy(type);

            if (Person.class.isAssignableFrom(partySubclass)) {
                String firstName = jsonNode.get("firstName").asText();
                String lastName = jsonNode.get("lastName").asText();

                PersonalData personalData = new PersonalData(firstName, lastName);

                return new Person(partyId, personalData, roles, registeredIdentifiers, version);
            } else if (Organization.class.isAssignableFrom(partySubclass)) {
                String organizationNameText = jsonNode.get("organizationName").asText();

                OrganizationName organizationName = new OrganizationName(organizationNameText);

                if (Company.class.isAssignableFrom(partySubclass)) {
                    return new Company(partyId, organizationName, roles, registeredIdentifiers, version);
                }
                if (OrganizationUnit.class.isAssignableFrom(partySubclass)) {
                    return new OrganizationUnit(partyId, organizationName, roles, registeredIdentifiers, version);
                }
            }

            throw new IllegalStateException("Cannot deserialize Party of type: " + type);
        }
    }

}
