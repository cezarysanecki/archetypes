package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public interface PartyRelationshipRepository {

    List<PartyRelationship> findAllRelationsFrom(PartyId partyId);

    List<PartyRelationship> findAllRelationsFrom(PartyId partyId, RelationshipName name);

    List<PartyRelationship> findAllRelationsFrom(PartyId partyId, Role role);

    Optional<PartyRelationship> findBy(PartyRelationshipId relationshipId);

    void save(PartyRelationship partyRelationship);

    Optional<PartyRelationshipId> delete(PartyRelationshipId relationshipId);

    List<PartyRelationship> findMatching(Predicate<PartyRelationship> predicate);

}

class InMemoryPartyRelationshipRepository implements PartyRelationshipRepository {

    private final ConcurrentHashMap<PartyRelationshipId, PartyRelationship> map = new ConcurrentHashMap<>(10);

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId) {
        return map.values().parallelStream()
                .filter(rel -> rel.from().partyId().equals(partyId))
                .toList();
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, RelationshipName name) {
        return map.values().parallelStream()
                .filter(rel -> rel.name().equals(name) && rel.from().partyId().equals(partyId))
                .toList();
    }

    @Override
    public List<PartyRelationship> findAllRelationsFrom(PartyId partyId, Role role) {
        return map.values().parallelStream()
                .filter(rel -> rel.from().role().equals(role) && rel.from().partyId().equals(partyId))
                .toList();
    }

    @Override
    public Optional<PartyRelationship> findBy(PartyRelationshipId relationshipId) {
        return Optional.ofNullable(map.get(relationshipId));
    }

    @Override
    public void save(PartyRelationship partyRelationship) {
        map.put(partyRelationship.id(), partyRelationship);
    }

    @Override
    public Optional<PartyRelationshipId> delete(PartyRelationshipId relationshipId) {
        PartyRelationship result = map.remove(relationshipId);
        return Optional.ofNullable(result).map(PartyRelationship::id);
    }

    @Override
    public List<PartyRelationship> findMatching(Predicate<PartyRelationship> predicate) {
        return map.values().parallelStream()
                .filter(predicate)
                .toList();
    }
}