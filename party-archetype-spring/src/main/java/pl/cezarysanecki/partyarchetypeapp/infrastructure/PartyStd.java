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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class PartyStd {

    static final Module PARTY_MODULE = new SimpleModule()
            .addSerializer(Party.class, new Serializer())
            .addDeserializer(Party.class, new Deserializer());

    private static final PartyFactory PARTY_FACTORY = new PartyFactory();
    private static final RegisteredIdentifiersFactory REGISTERED_IDENTIFIERS_FACTORY = new RegisteredIdentifiersFactory();

    private static final String ID = "id";
    private static final String REGISTERED_IDENTIFIERS_PREFIX = "registeredIdentifiers.";
    private static final String ROLES = "roles";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String TYPE = "type";
    private static final String VERSION = "version";

    static class Serializer extends StdSerializer<Party> {

        public Serializer() {
            super(Party.class);
        }

        @Override
        public void serialize(Party value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField(ID, value.partyId().asString());

            for (RegisteredIdentifier registeredIdentifier : value.registeredIdentifiers()) {
                gen.writeStringField(REGISTERED_IDENTIFIERS_PREFIX + registeredIdentifier.getType(), registeredIdentifier.getValue());
            }

            gen.writeArrayFieldStart(ROLES);
            for (Role role : value.roles()) {
                gen.writeString(role.name());
            }
            gen.writeEndArray();

            if (value instanceof Person person) {
                gen.writeStringField(FIRST_NAME, person.personalData().firstName());
                gen.writeStringField(LAST_NAME, person.personalData().lastName());
            }
            if (value instanceof Organization organization) {
                gen.writeStringField(ORGANIZATION_NAME, organization.organizationName().value());
            }

            gen.writeStringField(TYPE, value.getClass().getSimpleName());
            gen.writeNumberField(VERSION, value.version().value());
            gen.writeEndObject();
        }
    }

    static class Deserializer extends StdDeserializer<Party> {

        public Deserializer() {
            super(Party.class);
        }

        @Override
        public Party deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = p.getCodec().readTree(p);

            String type = jsonNode.get(TYPE).asText();
            PartyId partyId = PartyId.of(jsonNode.get(ID).asText());
            Version version = Version.of(jsonNode.get(VERSION).asLong());
            Set<Role> roles = StreamSupport.stream(jsonNode.get(ROLES).spliterator(), false)
                    .map(roleName -> new Role(roleName.asText()))
                    .collect(Collectors.toSet());
            Set<RegisteredIdentifier> registeredIdentifiers = jsonNode.properties().stream()
                    .filter(nodeEntry -> nodeEntry.getKey().startsWith(REGISTERED_IDENTIFIERS_PREFIX))
                    .map(nodeEntry -> REGISTERED_IDENTIFIERS_FACTORY.create(
                            nodeEntry.getKey().substring(REGISTERED_IDENTIFIERS_PREFIX.length()),
                            nodeEntry.getValue().asText()))
                    .collect(Collectors.toSet());

            Class<? extends Party> partySubclass = PARTY_FACTORY.findSubclassBy(type);

            if (Person.class.isAssignableFrom(partySubclass)) {
                String firstName = jsonNode.get(FIRST_NAME).asText();
                String lastName = jsonNode.get(LAST_NAME).asText();

                PersonalData personalData = new PersonalData(firstName, lastName);

                return new Person(partyId, personalData, roles, registeredIdentifiers, version);
            } else if (Organization.class.isAssignableFrom(partySubclass)) {
                String organizationNameText = jsonNode.get(ORGANIZATION_NAME).asText();

                OrganizationName organizationName = new OrganizationName(organizationNameText);

                if (Company.class.isAssignableFrom(partySubclass)) {
                    return new Company(partyId, organizationName, roles, registeredIdentifiers, version);
                } else if (OrganizationUnit.class.isAssignableFrom(partySubclass)) {
                    return new OrganizationUnit(partyId, organizationName, roles, registeredIdentifiers, version);
                }
            }

            throw new IllegalStateException("Cannot deserialize Party of type: " + type);
        }
    }

}
