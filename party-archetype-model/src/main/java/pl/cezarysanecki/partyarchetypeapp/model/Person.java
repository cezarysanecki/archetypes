package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;

public final class Person extends Party {

    private final PersonalData personalData;

    Person(PartyId id, PersonalData personalData, Set<Role> roles, Set<RegisteredIdentifier> registeredIdentifiers, Version version) {
        super(id, roles, registeredIdentifiers, version);
        
        checkArgument(personalData != null, "Personal data cannot be null");
        
        this.personalData = personalData;
    }

    public PersonalData personalData() {
        return this.personalData;
    }

    @Override
    PartyRegistered toPartyRegisteredEvent() {
        return new PartyRegistered.PersonRegistered(id().asString(), personalData().firstName(), personalData().lastName(),
                registeredIdentifiers().stream().map(RegisteredIdentifier::asString).collect(toSet()),
                roles().stream().map(Role::asString).collect(toSet()));
    }
}
