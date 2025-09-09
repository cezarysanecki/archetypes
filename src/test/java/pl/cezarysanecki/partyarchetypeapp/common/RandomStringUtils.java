package pl.cezarysanecki.partyarchetypeapp.common;

import java.util.Random;

public class RandomStringUtils {

    private static final Random RANDOM = new Random();

    public static String randomAlphabetic(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = (char) ('a' + RANDOM.nextInt(26));
            if (RANDOM.nextBoolean()) {
                ch = Character.toUpperCase(ch);
            }
            sb.append(ch);
        }
        return sb.toString();
    }

}
