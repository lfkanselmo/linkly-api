package com.linkly.domain.codec;

public final class Base62Codec {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    private Base62Codec() {
    }

    public static String encode(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative: " + value);
        }
        if (value == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }
        StringBuilder result = new StringBuilder();
        for (long remaining = value; remaining > 0; remaining /= BASE) {
            result.append(ALPHABET.charAt((int) (remaining % BASE)));
        }
        return result.reverse().toString();
    }

    public static long decode(String code) {
        long value = 0;
        for (int i = 0; i < code.length(); i++) {
            int digit = ALPHABET.indexOf(code.charAt(i));
            if (digit < 0) {
                throw new IllegalArgumentException("invalid Base62 character: " + code.charAt(i));
            }
            value = value * BASE + digit;
        }
        return value;
    }
}
