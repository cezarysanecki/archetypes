package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent;

import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.*;

public class PartiesFacade {

    private final PartyRepository partyRepository;
    private final EventPublisher eventPublisher;

    PartiesFacade(PartyRepository partyRepository, EventPublisher eventPublisher) {
        this.partyRepository = partyRepository;
        this.eventPublisher = eventPublisher;
    }

    public Result<PartyRelatedFailureEvent, Party> add(PartyId partyId, Role role) {
        return partyRepository.findBy(partyId)
                .map(party -> party.add(role))
                .map(party -> party.mapFailure(PartyRelatedFailureEvent.class::cast))
                .orElse(Result.failure(new PartyNotFound(partyId.asString())))
                .peekSuccess(partyRepository::save)
                .peekSuccess(party -> eventPublisher.publish(party.publishedEvents()));
    }

    public Result<PartyRelatedFailureEvent, Party> remove(PartyId partyId, Role role) {
        return partyRepository.findBy(partyId)
                .map(party -> party.remove(role))
                .map(party -> party.mapFailure(PartyRelatedFailureEvent.class::cast))
                .orElse(Result.failure(new PartyNotFound(partyId.asString())))
                .peekSuccess(partyRepository::save)
                .peekSuccess(party -> eventPublisher.publish(party.publishedEvents()));
    }

    public Result<PartyRelatedFailureEvent, Party> add(PartyId partyId, RegisteredIdentifier identifier) {
        return partyRepository.findBy(partyId)
                .map(party -> party.add(identifier))
                .map(party -> party.mapFailure(PartyRelatedFailureEvent.class::cast))
                .orElse(Result.failure(new RegisteredIdentifierAdditionFailed(partyId.asString(), identifier.asString(), "PARTY_NOT_FOUND")))
                .peekSuccess(partyRepository::save)
                .peekSuccess(party -> eventPublisher.publish(party.publishedEvents()));
    }

    public Result<PartyRelatedFailureEvent, Party> remove(PartyId partyId, RegisteredIdentifier identifier) {
        return partyRepository.findBy(partyId)
                .map(party -> party.remove(identifier))
                .map(party -> party.mapFailure(PartyRelatedFailureEvent.class::cast))
                .orElse(Result.failure(new RegisteredIdentifierRemovalFailed(partyId.asString(), identifier.asString(), "PARTY_NOT_FOUND")))
                .peekSuccess(partyRepository::save)
                .peekSuccess(party -> eventPublisher.publish(party.publishedEvents()));
    }

}
