package pl.cezarysanecki.partyarchetypeapp.common;

import java.util.Collection;

import static pl.cezarysanecki.partyarchetypeapp.common.RandomStringUtils.randomAlphabetic;
import static pl.cezarysanecki.partyarchetypeapp.common.RandomUtils.nextInt;

public class RandomFixture {

    public static <T> T randomElementOf(Collection<T> collection) {
        return collection.stream().toList().get(nextInt(0, collection.size()));
    }

    public static String randomStringWithPrefixOf(String prefix) {
        return prefix + "-" + randomAlphabetic(10);
    }
}
