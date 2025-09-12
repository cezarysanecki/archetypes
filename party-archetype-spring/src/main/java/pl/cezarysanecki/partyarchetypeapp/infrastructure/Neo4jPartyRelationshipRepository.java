package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipRepository;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
class Neo4jPartyRelationshipRepository implements PartyRelationshipRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(Neo4jPartyRelationshipRepository.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(PartyRelationshipStd.PARTY_RELATIONSHOP_MODULE);

    private final Neo4jClient neo4jClient;

    Neo4jPartyRelationshipRepository(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public void save(PartyRelationship partyRelationship) {
        neo4jClient.query(
                        "MATCH (from:Party {id: $fromId}) " +
                                "MATCH (to:Party {id: $toId}) " +
                                "MERGE (from)-[r:" + partyRelationship.name().asString() + " {id: $relId}]->(to) " +
                                "SET r += $props"
                )
                .bindAll(Map.of(
                        "fromId", partyRelationship.from().partyId().asString(),
                        "toId", partyRelationship.to().partyId().asString(),
                        "relId", partyRelationship.id().asString(),
                        "props", OBJECT_MAPPER.convertValue(partyRelationship, Map.class)
                ))
                .run();
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId) {
        return List.of();
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, RelationshipName name) {
        return List.of();
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, Role role) {
        return List.of();
    }

    @Override
    public Optional<PartyRelationship> findBy(PartyRelationshipId relationshipId) {
        return Optional.empty();
    }

    @Override
    public Optional<PartyRelationshipId> delete(PartyRelationshipId relationshipId) {
        return Optional.empty();
    }

    @Override
    public List<PartyRelationship> findMatching(Predicate<PartyRelationship> predicate) {
        return List.of();
    }
}
