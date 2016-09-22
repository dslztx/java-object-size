package sizeofagent;

import java.util.*;

public class StringGenerator {

    private static final int POOL_LEN = 62;
    private static final int STR_LEN_LIMIT = 50;
    private static final char[] CHAR_POOL = new char[POOL_LEN];

    private static final Random random = new Random();

    static {
        int index = 0;

        for (int offset = 0; offset < 26; offset++) {
            CHAR_POOL[index++] = (char) ('a' + offset);
        }

        for (int offset = 0; offset < 26; offset++) {
            CHAR_POOL[index++] = (char) ('A' + offset);
        }
        for (int offset = 0; offset < 10; offset++) {
            CHAR_POOL[index++] = (char) ('0' + offset);
        }
    }

    public static String[] generate(int len) {
        String[] result = new String[len];
        for (int index = 0; index < len; index++) {
            result[index] = generate();
        }

        return result;
    }

    private static String generate() {
        int len = random.nextInt(STR_LEN_LIMIT);

        if (len == 0) {
            len = 1;
        }

        StringBuilder sb = new StringBuilder();
        char c;
        for (int index = 0; index < len; index++) {
            c = CHAR_POOL[random.nextInt(POOL_LEN)];
            sb.append(c);
        }
        return sb.toString();
    }
}