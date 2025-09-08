package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedEvent;

import java.util.LinkedList;
import java.util.List;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;

public abstract class Party {

    private final PartyId partyId;

    private final List<PartyRelatedEvent> events = new LinkedList<>();
    private final Version version;

    Party(PartyId partyId, Version version) {
        checkArgument(partyId != null, "Party Id cannot be null");
        checkArgument(version != null, "Version cannot be null");
        this.partyId = partyId;
        this.version = version;
    }

    public PartyId partyId() {
        return partyId;
    }

    public List<PartyRelatedEvent> events() {
        return List.copyOf(events);
    }

    public Version version() {
        return version;
    }

}
