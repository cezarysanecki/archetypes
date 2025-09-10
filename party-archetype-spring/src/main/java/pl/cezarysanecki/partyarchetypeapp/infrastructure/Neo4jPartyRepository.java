package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.Party;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRepository;
import pl.cezarysanecki.partyarchetypeapp.model.PersonalData;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;
import pl.cezarysanecki.partyarchetypeapp.model.Role;
import pl.cezarysanecki.partyarchetypeapp.utils.PartyFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
class Neo4jPartyRepository implements PartyRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(Neo4jPartyRepository.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(
            new SimpleModule()
                    .addSerializer(Party.class, new StdSerializers.PartySerializer())
    );
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
                .map(Neo4jPartyRepository::createPartyFrom);
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

            LOGGER.info("Saving Party: {} with values: {}", party, values);

            neo4jClient.query("CREATE (n:Party) SET n = $props")
                    .bind(values).to("props")
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
                .map(Neo4jPartyRepository::createPartyFrom)
                .toList();
    }

    @Override
    public List<Party> findMatching(Predicate<Party> predicate) {
        return neo4jClient.query("MATCH (p:Party) RETURN p")
                .fetchAs(Map.class)
                .all()
                .stream()
                .map(Neo4jPartyRepository::createPartyFrom)
                .filter(predicate)
                .toList();
    }

    private static Party createPartyFrom(Map values) {
        String type = (String) values.get("type");
        return PARTY_FACTORY.create(type, values);
    }
}
