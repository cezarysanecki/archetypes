package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Pair;
import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyRelationshipDefinitionFailed;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.PartyRelationshipRemoved;

import java.util.function.BiFunction;

public class PartyRelationshipsFacade {

    private static final BiFunction<PartyRelatedFailureEvent, PartyRelatedFailureEvent, PartyRelatedFailureEvent> ANY_FAILURE = (fromFailure, toFailure) -> fromFailure != null ? fromFailure : toFailure;

    private final PartiesFacade partiesFacade;
    private final PartyRelationshipFactory partyRelationshipFactory;
    private final PartyRelationshipRepository repository;
    private final EventPublisher eventPublisher;

    PartyRelationshipsFacade(PartiesFacade partiesFacade, PartyRelationshipFactory partyRelationshipFactory, PartyRelationshipRepository repository, EventPublisher eventPublisher) {
        this.partiesFacade = partiesFacade;
        this.partyRelationshipFactory = partyRelationshipFactory;
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public Result<PartyRelatedFailureEvent, PartyRelationship> assign(PartyId fromId, Role fromRole, PartyId toId, Role toRole, RelationshipName name) {
        Result<PartyRelatedFailureEvent, Party> fromParty = partiesFacade.add(fromId, fromRole);
        Result<PartyRelatedFailureEvent, Party> toParty = partiesFacade.add(toId, toRole);

        return fromParty.combine(toParty, ANY_FAILURE, Pair::of)
                .flatMap(_ -> partyRelationshipFactory.defineFor(new PartyRole(fromId, fromRole), new PartyRole(toId, toRole), name)
                        .mapFailure(PartyRelatedFailureEvent.class::cast))
                .peekSuccess(repository::save)
                .peekSuccess(relation -> eventPublisher.publish(relation.toPartyRelationshipAddedEvent()));
    }

    public Result<PartyRelationshipDefinitionFailed, PartyRelationshipId> remove(PartyRelationshipId partyRelationshipId) {
        repository.delete(partyRelationshipId)
                .ifPresent(id -> eventPublisher.publish(new PartyRelationshipRemoved(id.asString())));
        return Result.success(partyRelationshipId);
    }


}
