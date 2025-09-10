package pl.cezarysanecki.partyarchetypeapp.model;

import pl.cezarysanecki.partyarchetypeapp.common.RandomFixture;
import pl.cezarysanecki.partyarchetypeapp.common.RandomStringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static pl.cezarysanecki.partyarchetypeapp.common.RandomStringUtils.randomAlphabetic;

class PartyFixture {

    static PersonTestDataBuilder somePerson() {
        return new PersonTestDataBuilder();
    }

    static CompanyTestDataBuilder someCompany() {
        return new CompanyTestDataBuilder();
    }

    @SuppressWarnings("unchecked")
    static <T extends Party> PartyAbstractTestDataBuilder<T> somePartyOfType(Class<T> clazz) {
        try {
            Class<?> testDataBuilder = Arrays.stream(PartyAbstractTestDataBuilder.class.getPermittedSubclasses()).filter(implementingClass -> {
                if (implementingClass.getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
                    return clazz.getTypeName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName());
                } else {
                    return false;
                }
            }).findFirst().orElseThrow(() -> new IllegalArgumentException("There is no party of type equal to " + clazz.getTypeName()));
            return (PartyAbstractTestDataBuilder<T>) testDataBuilder.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

class OrganizationNameFixture {

    static OrganizationName someOrganizationName() {
        return OrganizationName.of(RandomFixture.randomStringWithPrefixOf("organizationName"));
    }
}

class PersonalDataFixture {

    static PersonalData somePersonalData() {
        return PersonalData.from(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
    }

    static PersonalData nameOf(String firstName) {
        return PersonalData.from(firstName, null);
    }
}

final class RoleFixture {

    static Role someRole() {
        return Role.of(RandomStringUtils.randomAlphabetic(10));
    }

    static Set<Role> someRoleSetOfSize(int size) {
        return IntStream.range(0, size).mapToObj(it -> someRole()).collect(toSet());
    }

    static Set<String> stringSetFrom(Set<Role> roles) {
        return roles.stream().map(Role::asString).collect(toSet());
    }

}

final class RegisteredIdentifierFixture {

    static RegisteredIdentifier someRegisteredIdentifier() {
        return new RegisteredIdentifier() {

            private static final String TYPE = randomAlphabetic(10);
            private static final String VALUE = randomAlphabetic(10);

            @Override
            public String type() {
                return TYPE;
            }

            @Override
            public String value() {
                return VALUE;
            }
        };
    }

    static Set<RegisteredIdentifier> someIdentifierSetOfSize(int size) {
        return IntStream.range(0, size).mapToObj(_ -> someRegisteredIdentifier()).collect(toSet());
    }

}