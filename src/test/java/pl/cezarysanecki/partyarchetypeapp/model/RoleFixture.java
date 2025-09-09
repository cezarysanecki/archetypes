package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static pl.cezarysanecki.partyarchetypeapp.common.RandomStringUtils.randomAlphabetic;

final class RoleFixture {

    static String someRoleName() {
        return randomAlphabetic(10);
    }

    static Role someRole() {
        return Role.of(someRoleName());
    }

    static Set<Role> someRoleSetOfSize(int size) {
        return IntStream.range(0, size).mapToObj(it -> someRole()).collect(toSet());
    }

    static Set<String> stringSetFrom(Set<Role> roles) {
        return roles.stream().map(Role::asString).collect(toSet());
    }

}
