package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRepository;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static pl.cezarysanecki.partyarchetypeapp.infrastructure.PartyStd.PARTY_MODULE;

@Repository
class Neo4jPartyRepository implements PartyRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(Neo4jPartyRepository.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(PARTY_MODULE);

    private final Neo4jClient neo4jClient;

    public Neo4jPartyRepository(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public Optional<Party> findBy(PartyId partyId) {
        return neo4jClient.query("MATCH (p:Party {id: $id}) RETURN p")
                .bindAll(
                        Map.of("id", partyId.asString())
                )
                .fetchAs(Map.class)
                .one()
                .map(values -> OBJECT_MAPPER.convertValue(values, Party.class));
    }

    @Override
    public Optional<Party> findBy(PartyId partyId, Class<? extends Party> partyType) {
        return findBy(partyId)
                .filter(p -> partyType.isAssignableFrom(p.getClass()));
    }

    @Override
    public void save(Party party) {
        try {
            Map<String, Object> values = OBJECT_MAPPER.convertValue(party, Map.class);
            values.put("type", party.getClass().getSimpleName());

            LOGGER.debug("Saving Party: {} with values: {}", party, values);

            neo4jClient.query("MERGE (n:Party {id: $id}) SET n = $props")
                    .bindAll(Map.of(
                            "id", party.partyId().asString(),
                            "props", values
                    ))
                    .run();
        } catch (Exception e) {
            LOGGER.error("Could not save Party: {}", party, e);
            throw new RuntimeException("Cannot save Party " + party, e);
        }
    }

    @Override
    public void delete(PartyId partyId) {
        neo4jClient.query("MATCH (n:Party) WHERE ID(n) = $id DETACH DELETE n")
                .bind(partyId.asString()).to("id")
                .run();
    }

    @Override
    public List<Party> findBy(RegisteredIdentifier registeredIdentifier) {
        return neo4jClient.query(
                        "MATCH (p:Party) WHERE $type IN keys(p.registeredIdentifiers) AND p.registeredIdentifiers[$type] = $value RETURN p"
                )
                .bindAll(Map.of(
                        "type", registeredIdentifier.getType(),
                        "value", registeredIdentifier.getValue()
                ))
                .fetchAs(Map.class)
                .all()
                .stream()
                .map(values -> OBJECT_MAPPER.convertValue(values, Party.class))
                .toList();
    }

    @Override
    public List<Party> findMatching(Predicate<Party> predicate) {
        return neo4jClient.query("MATCH (p:Party) RETURN p")
                .fetchAs(Map.class)
                .all()
                .stream()
                .map(values -> OBJECT_MAPPER.convertValue(values, Party.class))
                .filter(predicate)
                .toList();
    }

}
