package pl.cezarysanecki.partyarchetypeapp.model.events;

import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;

import java.util.Set;

import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.CompanyRegistered;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.OrganizationUnitRegistered;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered.PersonRegistered;

public sealed interface PartyRegistered extends PartyRelatedEvent, PublishedEvent permits PersonRegistered, CompanyRegistered, OrganizationUnitRegistered {

    record PersonRegistered(String partyId,
                            String firstName,
                            String lastName,
                            Set<String> registeredIdentifiers,
                            Set<String> roles) implements PartyRegistered {
    }

    record CompanyRegistered(String partyId,
                             String organizationName,
                             Set<String> registeredIdentifiers,
                             Set<String> roles) implements PartyRegistered {
    }

    record OrganizationUnitRegistered(String partyId,
                                      String organizationName,
                                      Set<String> registeredIdentifiers,
                                      Set<String> roles) implements PartyRegistered {
    }

}