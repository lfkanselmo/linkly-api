package com.linkly.domain.codec;

import java.math.BigInteger;

public final class SequencePermutation {

    private static final BigInteger MODULUS = BigInteger.valueOf(4_294_967_291L);
    private static final BigInteger MULTIPLIER = BigInteger.valueOf(2_654_435_761L);

    private SequencePermutation() {
    }

    public static long permute(long sequenceValue) {
        if (sequenceValue < 0 || sequenceValue >= MODULUS.longValueExact()) {
            throw new IllegalArgumentException("sequenceValue out of range: " + sequenceValue);
        }
        return BigInteger.valueOf(sequenceValue).multiply(MULTIPLIER).mod(MODULUS).longValueExact();
    }
}
