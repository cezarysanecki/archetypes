package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.StringUtils.isNotBlank;

public record Role(String name) {

    public Role {
        checkArgument(isNotBlank(name), "Role name cannot be blank");
    }

    static Role of(String value) {
        return new Role(value);
    }

    String asString() {
        return name;
    }
}
