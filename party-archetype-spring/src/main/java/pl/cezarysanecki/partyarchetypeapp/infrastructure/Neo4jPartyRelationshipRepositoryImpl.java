package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
class Neo4jPartyRelationshipRepositoryImpl implements PartyRelationshipRepository {

    private final Neo4jPartySpringRepository neo4jPartySpringRepository;
    private final Neo4jPartyRelationshipRepository neo4jPartyRelationshipRepository;

    Neo4jPartyRelationshipRepositoryImpl(Neo4jPartySpringRepository neo4jPartySpringRepository, Neo4jPartyRelationshipRepository neo4jPartyRelationshipRepository) {
        this.neo4jPartySpringRepository = neo4jPartySpringRepository;
        this.neo4jPartyRelationshipRepository = neo4jPartyRelationshipRepository;
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId) {
        return neo4jPartyRelationshipRepository.findAll().stream()
                .filter(e -> e.getFrom() != null && e.getFrom().getId().equals(partyId.asString()))
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, RelationshipName name) {
        return neo4jPartyRelationshipRepository.findAll().stream()
                .filter(e -> e.getFrom() != null && e.getFrom().getId().equals(partyId.asString()) && e.getRelationshipName().equals(name.value()))
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, Role role) {
        return neo4jPartyRelationshipRepository.findAll().stream()
                .filter(e -> e.getFrom() != null && e.getFrom().getId().equals(partyId.asString()) && e.getFrom().getRoles().contains(role.name()))
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PartyRelationship> findBy(PartyRelationshipId relationshipId) {
        try {
            String id = relationshipId.asString();
            return neo4jPartyRelationshipRepository.findById(id).map(Neo4jPartyRelationshipMapper::toDomain);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(PartyRelationship partyRelationship) {
        Neo4jPartyEntity from = neo4jPartySpringRepository.findById(partyRelationship.from().partyId().asString()).orElseThrow();
        Neo4jPartyEntity to = neo4jPartySpringRepository.findById(partyRelationship.to().partyId().asString()).orElseThrow();
        neo4jPartyRelationshipRepository.save(Neo4jPartyRelationshipMapper.toEntity(partyRelationship, from, to));
    }

    @Override
    public Optional<PartyRelationshipId> delete(PartyRelationshipId relationshipId) {
        try {
            String id = relationshipId.asString();
            neo4jPartyRelationshipRepository.deleteById(id);
            return Optional.of(relationshipId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PartyRelationship> findMatching(Predicate<PartyRelationship> predicate) {
        return neo4jPartyRelationshipRepository.findAll().stream()
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .filter(predicate)
                .collect(Collectors.toList());
    }
}

