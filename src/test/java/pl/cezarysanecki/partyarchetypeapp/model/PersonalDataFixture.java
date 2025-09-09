package pl.cezarysanecki.partyarchetypeapp.model;

import static pl.cezarysanecki.partyarchetypeapp.common.RandomStringUtils.randomAlphabetic;

class PersonalDataFixture {

    static String someFirstName() {
        return randomAlphabetic(10);
    }

    static String someLastName() {
        return randomAlphabetic(10);
    }

    static PersonalData somePersonalData() {
        return PersonalData.from(someFirstName(), someLastName());
    }

    static PersonalData nameOf(String firstName) {
        return PersonalData.from(firstName, null);
    }
}
