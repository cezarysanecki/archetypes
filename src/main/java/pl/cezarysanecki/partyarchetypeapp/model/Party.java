package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.common.Version;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RoleAdditionFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent.RoleRemovalFailed;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleAdded;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleAdditionSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleRemovalSkipped;
import static pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedSucceededEvent.RoleRemoved;

public abstract class Party {

    private final PartyId partyId;
    private final Set<Role> roles;

    private final List<PartyRelatedEvent> events = new LinkedList<>();
    private final Version version;

    Party(PartyId partyId, Set<Role> roles, Version version) {
        checkArgument(partyId != null, "Party Id cannot be null");
        checkArgument(version != null, "Version cannot be null");
        checkArgument(roles != null, "Roles cannot be null");

        this.partyId = partyId;
        this.roles = roles;

        this.version = version;
    }

    public Result<RoleAdditionFailed, Party> add(Role role) {
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

    public PartyId partyId() {
        return partyId;
    }

    public Set<Role> roles() {
        return Set.copyOf(roles);
    }

    public List<PartyRelatedEvent> events() {
        return List.copyOf(events);
    }

    public Version version() {
        return version;
    }

}
