package pl.cezarysanecki.partyarchetypeapp.common;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static int nextInt(int startInclusive, int endExclusive) {
        Preconditions.checkArgument(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Preconditions.checkArgument(startInclusive >= 0, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

}
