package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Result;

import java.util.function.Supplier;

import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyRelationshipDefinitionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyRelationshipDefinitionFailed.dueToPoliciesNotMet;

public class PartyRelationshipFactory {

    private static final PartyRelationshipDefiningPolicy DEFAULT_PARTY_RELATIONSHIP_DEFINING_POLICY = new AlwaysAllowPartyRelationshipDefiningPolicy();
    private final PartyRelationshipDefiningPolicy policy;
    private final Supplier<PartyRelationshipId> partyRelationshipIdSupplier;

    public PartyRelationshipFactory(PartyRelationshipDefiningPolicy policy, Supplier<PartyRelationshipId> partyRelationshipIdSupplier) {
        this.policy = policy != null ? policy : DEFAULT_PARTY_RELATIONSHIP_DEFINING_POLICY;
        this.partyRelationshipIdSupplier = partyRelationshipIdSupplier != null ? partyRelationshipIdSupplier : PartyRelationshipId::random;
    }

    public PartyRelationshipFactory(Supplier<PartyRelationshipId> partyRelationshipIdSupplier) {
        this(null, partyRelationshipIdSupplier);
    }

    Result<PartyRelationshipDefinitionFailed, PartyRelationship> defineFor(PartyRole from, PartyRole to, RelationshipName name) {
        if (policy.canDefineFor(from, to, name)) {
            return Result.success(PartyRelationship.from(partyRelationshipIdSupplier.get(), from, to, name));
        } else {
            return Result.failure(dueToPoliciesNotMet());
        }
    }
}
