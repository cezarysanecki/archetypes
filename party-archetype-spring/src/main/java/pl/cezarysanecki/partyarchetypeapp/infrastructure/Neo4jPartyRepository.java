package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRepository;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.utils.PartyFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
class Neo4jPartyRepository implements PartyRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static PartyFactory PARTY_FACTORY = new PartyFactory();

    private final Neo4jClient neo4jClient;

    public Neo4jPartyRepository(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public Optional<Party> findBy(PartyId partyId) {
        return neo4jClient.query("MATCH (p:Party {id: $id}) RETURN p")
                .bindAll(Map.of("id", partyId.asString()))
                .fetchAs(Map.class)
                .one()
                .map(
                        values -> {
                            String type = (String) values.get("type");
                            return PARTY_FACTORY.create(type, values);
                        }
                );
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

            neo4jClient.query("CREATE (n:Party) SET n = $props")
                    .bind(values).to("props")
                    .run();
        } catch (Exception e) {
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
        return neo4jPartySpringRepository.findAll().stream()
                .filter(e -> e.registeredIdentifiers.entrySet().stream()
                        .anyMatch(entrySet -> entrySet.getKey().equals(registeredIdentifier.type()) && entrySet.getValue().equals(registeredIdentifier.value())))
                .map(Neo4jPartyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Party> findMatching(Predicate<Party> predicate) {
        return neo4jPartySpringRepository.findAll().stream()
                .map(Neo4jPartyMapper::toDomain)
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
