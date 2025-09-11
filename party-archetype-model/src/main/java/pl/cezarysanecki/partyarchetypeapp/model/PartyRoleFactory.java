package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Result;

import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyRoleDefinitionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyRoleDefinitionFailed.dueToPoliciesNotMet;

public class PartyRoleFactory {

    private static final PartyRoleDefiningPolicy DEFAULT_PARTY_ROLE_DEFINING_POLICY = new AlwaysAllowPartyRoleDefiningPolicy();

    private final PartyRoleDefiningPolicy policy;

    PartyRoleFactory(PartyRoleDefiningPolicy policy) {
        this.policy = policy != null ? policy : DEFAULT_PARTY_ROLE_DEFINING_POLICY;
    }

    public PartyRoleFactory() {
        this(null);
    }

    Result<PartyRoleDefinitionFailed, PartyRole> defineFor(Party party, Role role) {
        if (policy.canDefineFor(party, role)) {
            return Result.success(PartyRole.of(party.partyId(), role));
        } else {
            return Result.failure(dueToPoliciesNotMet());
        }
    }
}
