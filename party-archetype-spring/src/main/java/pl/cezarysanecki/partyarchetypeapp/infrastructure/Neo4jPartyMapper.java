package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import pl.cezarysanecki.partyarchetypeapp.model.Company;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationUnit;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.Person;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.model.Role;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.utils.RegisteredIdentifiersFactory;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class Neo4jPartyMapper {

    private static final RegisteredIdentifiersFactory REGISTERED_IDENTIFIERS_FACTORY = new RegisteredIdentifiersFactory();

    static Neo4jPartyEntity toEntity(Party party) {
        return new Neo4jPartyEntity(
                party.id().asString(),
                party.getClass().getSimpleName(),
                party.roles().stream().map(Role::name).collect(Collectors.toSet()),
                party.registeredIdentifiers().stream()
                        .map(id -> new Neo4jPartyEntity.Neo4jRegisteredIdentifierEntity(id.type(), id.value()))
                        .collect(Collectors.toSet()),
                party.version().toString()
        );
    }

    static Party toDomain(Neo4jPartyEntity entity) {
        Set<Role> roles = entity.getRoles().stream().map(Role::new).collect(Collectors.toSet());
        Set<RegisteredIdentifier> ids = entity.getRegisteredIdentifiers().stream()
                .map(e -> REGISTERED_IDENTIFIERS_FACTORY.create(e.getType(), e.getValue()))
                .collect(Collectors.toSet());
        Version version = Version.of(Long.parseLong(entity.getVersion()));
        PartyId partyId = PartyId.of(UUID.fromString(entity.getId()));
        return switch (entity.getType()) {
            case "Person" -> new Person(partyId, null, roles, ids, version);
            case "Company" -> new Company(partyId, null, roles, ids, version);
            case "OrganizationUnit" -> new OrganizationUnit(partyId, null, roles, ids, version);
            default -> throw new IllegalArgumentException("Unknown party type: " + entity.getType());
        };
    }
}
