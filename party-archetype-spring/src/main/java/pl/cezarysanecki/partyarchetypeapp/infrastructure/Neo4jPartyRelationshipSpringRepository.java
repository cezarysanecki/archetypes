package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.repository.Neo4jRepository;

interface Neo4jPartyRelationshipSpringRepository extends Neo4jRepository<Neo4jPartyRelationshipEntity, String> {
}

