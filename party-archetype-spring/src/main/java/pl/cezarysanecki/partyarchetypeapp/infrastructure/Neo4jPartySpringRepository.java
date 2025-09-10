package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.repository.Neo4jRepository;

interface Neo4jPartySpringRepository extends Neo4jRepository<Neo4jPartyEntity, String> {
}

