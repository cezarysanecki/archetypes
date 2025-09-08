package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.common.events.PublishedEvent;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRegistered;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RegisteredIdentifierAdditionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RegisteredIdentifierRemovalFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RoleAdditionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RoleRemovalFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierAdded;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierAdditionSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierRemovalSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RegisteredIdentifierRemoved;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleAdded;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleAdditionSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleRemovalSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleRemoved;

public abstract class Party {

    private final PartyId partyId;
    private final Set<Role> roles;
    private final Set<RegisteredIdentifier> registeredIdentifiers;

    private final List<PartyRelatedEvent> events = new LinkedList<>();
    private final Version version;

    Party(PartyId partyId, Set<Role> roles, Set<RegisteredIdentifier> registeredIdentifiers, Version version) {
        checkArgument(partyId != null, "Party Id cannot be null");
        checkArgument(version != null, "Version cannot be null");
        checkArgument(roles != null, "Roles cannot be null");
        checkArgument(registeredIdentifiers != null, "Registered Identifiers cannot be null");

        this.partyId = partyId;
        this.roles = roles;
        this.registeredIdentifiers = registeredIdentifiers;

        this.version = version;
    }

    Result<RoleAdditionFailed, Party> add(Role role) {
        checkNotNull(role, "Role cannot be null");

        if (!roles.contains(role)) {
            roles.add(role);
            events.add(new RoleAdded(partyId.asString(), role.asString()));
        } else {
            events.add(RoleAdditionSkipped.dueToDuplicationFor(partyId.asString(), role.asString()));
        }
        return Result.success(this);
    }

    Result<RoleRemovalFailed, Party> remove(Role role) {
        checkNotNull(role, "Role cannot be null");

        if (roles.contains(role)) {
            roles.remove(role);
            events.add(new RoleRemoved(partyId.asString(), role.asString()));
        } else {
            events.add(RoleRemovalSkipped.dueToMissingRoleFor(partyId.asString(), role.asString()));
        }
        return Result.success(this);
    }

    Result<RegisteredIdentifierAdditionFailed, Party> add(RegisteredIdentifier identifier) {
        checkNotNull(identifier, "Registered identifier cannot be null");

        if (!registeredIdentifiers.contains(identifier)) {
            registeredIdentifiers.add(identifier);
            events.add(new RegisteredIdentifierAdded(partyId.asString(), identifier.type(), identifier.asString()));
        } else {
            events.add(RegisteredIdentifierAdditionSkipped.dueToDataDuplicationFor(partyId.asString(), identifier.type(), identifier.asString()));
        }
        return Result.success(this);
    }

    Result<RegisteredIdentifierRemovalFailed, Party> remove(RegisteredIdentifier identifier) {
        checkNotNull(identifier, "Registered identifier cannot be null");

        if (registeredIdentifiers.contains(identifier)) {
            registeredIdentifiers.remove(identifier);
            events.add(new RegisteredIdentifierRemoved(partyId.asString(), identifier.type(), identifier.asString()));
        } else {
            events.add(RegisteredIdentifierRemovalSkipped.dueToMissingIdentifierFor(partyId.asString(), identifier.type(), identifier.asString()));
        }
        return Result.success(this);
    }

    public PartyId id() {
        return partyId;
    }

    public Set<Role> roles() {
        return Set.copyOf(roles);
    }

    public Set<RegisteredIdentifier> registeredIdentifiers() {
        return Set.copyOf(registeredIdentifiers);
    }

    public List<PartyRelatedEvent> events() {
        return List.copyOf(events);
    }

    public List<PublishedEvent> publishedEvents() {
        return events.stream().filter(PublishedEvent.class::isInstance).map(PublishedEvent.class::cast).toList();
    }

    public Version version() {
        return version;
    }

    abstract PartyRegistered toPartyRegisteredEvent();

    final void register(PartyRelatedEvent event) {
        events.add(event);
    }

}
