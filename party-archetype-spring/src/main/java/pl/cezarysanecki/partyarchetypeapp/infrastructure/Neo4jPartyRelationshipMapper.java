package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRole;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.UUID;

class Neo4jPartyRelationshipMapper {

    static PartyRelationship toDomain(Neo4jPartyRelationshipEntity entity) {
        PartyRelationshipId partyRelationshipId = PartyRelationshipId.of(UUID.fromString(entity.getId()));
        return new PartyRelationship(
                partyRelationshipId,
                new PartyRole(new PartyId(UUID.fromString(entity.getFrom().getId())), new Role(entity.getFromRole())),
                new PartyRole(new PartyId(UUID.fromString(entity.getTo().getId())), new Role(entity.getToRole())),
                new RelationshipName(entity.getRelationshipName())
        );
    }

    static Neo4jPartyRelationshipEntity toEntity(PartyRelationship relationship, Neo4jPartyEntity from, Neo4jPartyEntity to) {
        return new Neo4jPartyRelationshipEntity(
                relationship.id().asString(),
                relationship.name().value(),
                from,
                relationship.from().role().name(),
                to,
                relationship.to().role().name()
        );
    }
}
