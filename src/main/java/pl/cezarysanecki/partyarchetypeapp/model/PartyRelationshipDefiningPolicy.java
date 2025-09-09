package pl.cezarysanecki.partyarchetypeapp.model;

interface PartyRelationshipDefiningPolicy {

    boolean canDefineFor(PartyRole from, PartyRole to, RelationshipName relationshipName);
}

final class AlwaysAllowPartyRelationshipDefiningPolicy implements PartyRelationshipDefiningPolicy {

    @Override
    public boolean canDefineFor(PartyRole from, PartyRole to, RelationshipName relationshipName) {
        return true;
    }
}
