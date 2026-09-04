package com.linkly.domain.codec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Base62CodecTest {

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 61L, 62L, 100_000L, 4_294_967_290L, Long.MAX_VALUE})
    void roundTripsAnyNonNegativeValue(long value) {
        assertThat(Base62Codec.decode(Base62Codec.encode(value))).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 61L, 62L, 123_456L})
    void encodedValueUsesOnlyBase62Alphabet(long value) {
        assertThat(Base62Codec.encode(value)).matches("^[0-9A-Za-z]+$");
    }

    @Test
    void rejectsNegativeValues() {
        assertThatThrownBy(() -> Base62Codec.encode(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsInvalidCharactersOnDecode() {
        assertThatThrownBy(() -> Base62Codec.decode("abc!"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
