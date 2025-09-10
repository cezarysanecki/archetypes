package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
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
            gen.writeStringField("id", value.getPartyId().asString());

            for (RegisteredIdentifier registeredIdentifier : value.getRegisteredIdentifiers()) {
                gen.writeStringField("registeredIdentifiers." + registeredIdentifier.getType(), registeredIdentifier.getValue());
            }

            gen.writeArrayFieldStart("roles");
            for (Role role : value.getRoles()) {
                gen.writeString(role.name());
            }
            gen.writeEndArray();

            try {
                if (value.getClass().getMethod("getPersonalData") != null) {
                    Method method = value.getClass().getMethod("getPersonalData");
                    Object returnValue = method.invoke(value);
                    if (returnValue instanceof PersonalData personalData) {
                        gen.writeStringField("firstName", personalData.firstName());
                        gen.writeStringField("lastName", personalData.lastName());
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (value.getClass().getMethod("getOrganizationName") != null) {
                    Method method = value.getClass().getMethod("getOrganizationName");
                    Object returnValue = method.invoke(value);
                    if (returnValue instanceof OrganizationName organizationName) {
                        gen.writeStringField("organizationName", organizationName.value());
                    }
                }
            } catch (Exception e) {
            }

            gen.writeStringField("type", value.getClass().getSimpleName());
            gen.writeNumberField("version", value.getVersion().value());
            gen.writeEndObject();
        }
    }

}
