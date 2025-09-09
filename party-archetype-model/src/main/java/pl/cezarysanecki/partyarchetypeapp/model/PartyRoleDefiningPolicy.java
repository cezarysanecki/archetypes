package pl.cezarysanecki.partyarchetypeapp.model;

@FunctionalInterface
interface PartyRoleDefiningPolicy {

    boolean canDefineFor(Party party, Role role);

}

final class AlwaysAllowPartyRoleDefiningPolicy implements PartyRoleDefiningPolicy {

    @Override
    public boolean canDefineFor(Party party, Role role) {
        return true;
    }
}
