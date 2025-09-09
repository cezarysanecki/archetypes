package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Pair;
import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.common.events.EventPublisher;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.*;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.PartyNotFound;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RegisteredIdentifierAdditionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RegisteredIdentifierRemovalFailed;

public class PartiesFacade {
    private static final BiFunction<PartyRelatedFailureEvent, PartyRelatedFailureEvent, PartyRelatedFailureEvent> ANY_FAILURE = (fromFailure, toFailure) -> fromFailure != null ? fromFailure : toFailure;

    private final PartyRepository partyRepository;
    private final EventPublisher eventPublisher;
    private final PartyRoleFactory partyRoleFactory;
    private final Supplier<PartyId> newPartyIdSupplier;

    PartiesFacade(PartyRepository partyRepository, EventPublisher eventPublisher, PartyRoleFactory partyRoleFactory, Supplier<PartyId> newPartyIdSupplier) {
        this.partyRepository = partyRepository;
        this.eventPublisher = eventPublisher;
        this.partyRoleFactory = partyRoleFactory;
        this.newPartyIdSupplier = newPartyIdSupplier != null ? newPartyIdSupplier : PartyId::random;
    }

    public Result<PartyRelatedFailureEvent, Person> registerPersonFor(PersonalData personalData, Set<Role> roles, Set<RegisteredIdentifier> registeredIdentifiers) {
        return registerPartyAccordingTo(() -> new Person(newPartyIdSupplier.get(), personalData, roles, registeredIdentifiers, Version.initial())).map(Person.class::cast);
    }

    public Result<PartyRelatedFailureEvent, Company> registerCompanyFor(OrganizationName organizationName, Set<Role> roles, Set<RegisteredIdentifier> registeredIdentifiers) {
        return registerPartyAccordingTo(() -> new Company(newPartyIdSupplier.get(), organizationName, roles, registeredIdentifiers, Version.initial())).map(Company.class::cast);
    }

    public Result<PartyRelatedFailureEvent, OrganizationUnit> registerOrganizationUnitFor(OrganizationName organizationName, Set<Role> roles, Set<RegisteredIdentifier> registeredIdentifiers) {
        return registerPartyAccordingTo(() -> new OrganizationUnit(newPartyIdSupplier.get(), organizationName, roles, registeredIdentifiers, Version.initial())).map(OrganizationUnit.class::cast);
    }

    public Result<PartyRelatedFailureEvent, Party> add(PartyId partyId, Role role) {
        return partyRepository.findBy(partyId)
                .map(party -> partyRoleFactory.defineFor(party, role)
                        .mapFailure(PartyRelatedFailureEvent.class::cast)
                        .combineOther(party.add(role).mapFailure(PartyRelatedFailureEvent.class::cast), ANY_FAILURE, Pair::of))
                .orElse(Result.failure(new PartyNotFound(partyId.asString())))
                .map(Pair::getSecond)
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

    private Result<PartyRelatedFailureEvent, Party> registerPartyAccordingTo(Supplier<Party> partySupplier) {
        try {
            Party party = partySupplier.get();
            partyRepository.save(party);
            eventPublisher.publish(party.toPartyRegisteredEvent());
            return Result.success(party);
        } catch (Exception ex) {
            return Result.failure(PartyRegistrationFailed.dueTo(ex));
        }
    }

}
