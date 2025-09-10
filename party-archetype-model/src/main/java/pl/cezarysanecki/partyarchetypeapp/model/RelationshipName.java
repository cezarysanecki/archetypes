package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.StringUtils.isNotBlank;

public record RelationshipName(String value) {

    public RelationshipName {
        checkArgument(isNotBlank(value), "Relationship name cannot be null");
    }

    static RelationshipName of(String value) {
        return new RelationshipName(value);
    }

    public String asString() {
        return value;
    }

}
