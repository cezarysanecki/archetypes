package pl.cezarysanecki.partyarchetypeapp.utils;

import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.model.Company;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationUnit;
import pl.cezarysanecki.partyarchetypeapp.model.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartyFactoryTest {

    private final PartyFactory sut = new PartyFactory();

    @Test
    void shouldFindPersonSubclassByType() {
        //when
        Class<?> result = sut.findSubclassBy("Person");

        //then
        assertEquals(Person.class, result);
    }

    @Test
    void shouldFindCompanySubclassByType() {
        //when
        Class<?> result = sut.findSubclassBy("Company");

        //then
        assertEquals(Company.class, result);
    }

    @Test
    void shouldFindOrganizationUnitSubclassByType() {
        //when
        Class<?> result = sut.findSubclassBy("OrganizationUnit");

        //then
        assertEquals(OrganizationUnit.class, result);
    }

}