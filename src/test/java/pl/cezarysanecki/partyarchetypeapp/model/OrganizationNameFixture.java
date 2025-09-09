package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.RandomFixture;

class OrganizationNameFixture {

    static OrganizationName someOrganizationName() {
        return OrganizationName.of(RandomFixture.randomStringWithPrefixOf("organizationName"));
    }
}
