package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.regex.Pattern;

import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkArgument;
import static pl.cezarysanecki.partyarchetypeapp.common.Preconditions.checkNotNull;

public interface RegisteredIdentifier {

    String type();

    String asString();

}

record PersonalIdentificationNumber(String value) implements RegisteredIdentifier {

    private static final Pattern PATTERN = Pattern.compile("\\d{11}");
    private static final String TYPE = "PERSONAL_IDENTIFICATION_NUMBER";

    PersonalIdentificationNumber {
        checkNotNull(value, "Personal Identification Number cannot be null");
        checkArgument(!PATTERN.matcher(value).matches(), "Personal identification number does not meet syntax criteria");
    }

    static PersonalIdentificationNumber of(String value) {
        return new PersonalIdentificationNumber(value);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String asString() {
        return value;
    }
}