package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.List;
import java.util.Optional;
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
