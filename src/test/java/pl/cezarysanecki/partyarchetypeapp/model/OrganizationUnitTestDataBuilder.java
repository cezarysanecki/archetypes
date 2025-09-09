package pl.cezarysanecki.partyarchetypeapp.model;

final class OrganizationUnitTestDataBuilder extends PartyAbstractTestDataBuilder<OrganizationUnit> {

    OrganizationName organizationName;

    OrganizationUnitTestDataBuilder with(OrganizationName organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    @Override
    OrganizationUnit build() {
        return new OrganizationUnit(partyId, organizationName, roles, registeredIdentifiers, version);
    }
}
