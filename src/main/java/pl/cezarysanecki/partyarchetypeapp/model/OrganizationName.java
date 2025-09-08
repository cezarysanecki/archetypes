package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.StringUtils.isNotBlank;

public record OrganizationName(String value) {

    public OrganizationName {
        checkArgument(isNotBlank(value), "Organization name cannot be blank");
    }

    static OrganizationName of(String value) {
        return new OrganizationName(value);
    }

    String asString() {
        return value;
    }

}
