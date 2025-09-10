package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
class Neo4jPartyRepository implements PartyRepository {

    private final Neo4jPartySpringRepository neo4jPartySpringRepository;

    public Neo4jPartyRepository(Neo4jPartySpringRepository neo4jPartySpringRepository) {
        this.neo4jPartySpringRepository = neo4jPartySpringRepository;
    }

    @Override
    public Optional<Party> findBy(PartyId partyId) {
        return neo4jPartySpringRepository.findById(partyId.asString())
                .map(Neo4jPartyMapper::toDomain);
    }

    @Override
    public Optional<Party> findBy(PartyId partyId, Class<? extends Party> partyType) {
        return findBy(partyId).filter(p -> partyType.isAssignableFrom(p.getClass()));
    }

    @Override
    public void save(Party party) {
        neo4jPartySpringRepository.save(Neo4jPartyMapper.toEntity(party));
    }

    @Override
    public void delete(PartyId partyId) {
        neo4jPartySpringRepository.deleteById(partyId.asString());
    }

    @Override
    public List<Party> findBy(RegisteredIdentifier registeredIdentifier) {
        return neo4jPartySpringRepository.findAll().stream()
                .filter(e -> e.getRegisteredIdentifiers().entrySet().stream()
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
