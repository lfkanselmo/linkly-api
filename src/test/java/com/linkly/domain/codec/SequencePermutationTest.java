package com.linkly.domain.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

class SequencePermutationTest {

    @Test
    void producesNoCollisionsAcrossConsecutiveSequenceValues() {
        Set<Long> permuted = new HashSet<>();
        LongStream.range(100_000, 200_000).forEach(value -> permuted.add(SequencePermutation.permute(value)));
        assertThat(permuted).hasSize(100_000);
    }

    @Test
    void consecutiveInputsDoNotProduceConsecutiveOutputs() {
        long first = SequencePermutation.permute(100_000);
        long second = SequencePermutation.permute(100_001);
        assertThat(Math.abs(second - first)).isGreaterThan(1);
    }

    @Test
    void rejectsValuesOutOfRange() {
        assertThatThrownBy(() -> SequencePermutation.permute(-1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> SequencePermutation.permute(4_294_967_291L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
