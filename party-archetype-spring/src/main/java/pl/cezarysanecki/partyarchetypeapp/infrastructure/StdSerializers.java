package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.cezarysanecki.partyarchetypeapp.model.Organization;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.Person;
import pl.cezarysanecki.partyarchetypeapp.model.PersonalData;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.io.IOException;
import java.lang.reflect.Method;

class StdSerializers {

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

}
