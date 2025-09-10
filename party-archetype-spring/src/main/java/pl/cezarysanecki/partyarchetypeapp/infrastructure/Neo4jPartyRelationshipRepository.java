package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.stereotype.Repository;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationship;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipId;
import pl.cezarysanecki.partyarchetypeapp.model.PartyRelationshipRepository;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
class Neo4jPartyRelationshipRepository implements PartyRelationshipRepository {

    private final Neo4jPartySpringRepository neo4jPartySpringRepository;

    Neo4jPartyRelationshipRepository(Neo4jPartySpringRepository neo4jPartySpringRepository) {
        this.neo4jPartySpringRepository = neo4jPartySpringRepository;
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId) {
        return neo4jPartySpringRepository.findById(partyId.asString())
                .map(Neo4jPartyEntity::getRelationships)
                .orElseGet(Set::of)
                .stream()
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, RelationshipName name) {
        return neo4jPartySpringRepository.findById(partyId.asString())
                .map(Neo4jPartyEntity::getRelationships)
                .orElseGet(Set::of)
                .stream()
                .filter(rel -> rel.getRelationshipName().equals(name.value()))
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, Role role) {
        return neo4jPartySpringRepository.findById(partyId.asString())
                .map(Neo4jPartyEntity::getRelationships)
                .orElseGet(Set::of)
                .stream()
                .filter(rel -> rel.getFromRole().equals(role.name()))
                .map(Neo4jPartyRelationshipMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<PartyRelationship> findBy(PartyRelationshipId relationshipId) {
        return neo4jPartySpringRepository.findAll()
                .stream()
                .map(Neo4jPartyEntity::getRelationships)
                .flatMap(Collection::stream)
                .filter(rel -> rel.getRelationShipId().equals(relationshipId.asString()))
                .findFirst()
                .map(Neo4jPartyRelationshipMapper::toDomain);
    }

    @Override
    public void save(PartyRelationship partyRelationship) {
        Neo4jPartyEntity from = neo4jPartySpringRepository.findById(partyRelationship.from().partyId().asString()).orElseThrow();
        Neo4jPartyEntity to = neo4jPartySpringRepository.findById(partyRelationship.to().partyId().asString()).orElseThrow();
        from.addRelationship(Neo4jPartyRelationshipMapper.toEntity(partyRelationship, to));
        neo4jPartySpringRepository.save(from);
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

