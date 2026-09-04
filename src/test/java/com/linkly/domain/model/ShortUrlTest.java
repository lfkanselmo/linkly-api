package com.linkly.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.linkly.domain.exception.InvalidShortUrlException;

class ShortUrlTest {

    @Test
    void createsWithValidData() {
        ShortUrl shortUrl = new ShortUrl(1, "aB3xQ", "https://example.com/page", Instant.now(), null);
        assertThat(shortUrl.shortCode()).isEqualTo("aB3xQ");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ftp://example.com", "example.com", "javascript:alert(1)"})
    void rejectsNonHttpUrls(String url) {
        assertThatThrownBy(() -> new ShortUrl(1, "aB3xQ", url, Instant.now(), null))
                .isInstanceOf(InvalidShortUrlException.class);
    }

    @Test
    void rejectsUrlsWithInvalidUriSyntax() {
        assertThatThrownBy(() -> new ShortUrl(1, "aB3xQ", "https://example.com/hello world", Instant.now(), null))
                .isInstanceOf(InvalidShortUrlException.class);
    }

    @Test
    void rejectsBlankShortCode() {
        assertThatThrownBy(() -> new ShortUrl(1, " ", "https://example.com", Instant.now(), null))
                .isInstanceOf(InvalidShortUrlException.class);
    }

    @Test
    void isExpiredWhenPastExpiresAt() {
        Instant now = Instant.now();
        ShortUrl shortUrl = new ShortUrl(1, "aB3xQ", "https://example.com", now, now.minus(1, ChronoUnit.DAYS));
        assertThat(shortUrl.isExpired(now)).isTrue();
    }

    @Test
    void isNotExpiredWithoutExpiresAt() {
        Instant now = Instant.now();
        ShortUrl shortUrl = new ShortUrl(1, "aB3xQ", "https://example.com", now, null);
        assertThat(shortUrl.isExpired(now)).isFalse();
    }
}
