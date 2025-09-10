package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface Neo4jPartyRelationshipRepository extends Neo4jRepository<Neo4jPartyRelationshipEntity, String> {
}

