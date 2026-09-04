package com.linkly.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class JpaUrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void savesAndFindsByShortCode() {
        String shortCode = uniqueCode();
        Instant createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        ShortUrl saved = urlRepository.save(
                new ShortUrl(0, shortCode, "https://example.com/page", createdAt, null));

        assertThat(saved.id()).isPositive();
        assertThat(urlRepository.findByShortCode(shortCode))
                .contains(new ShortUrl(saved.id(), shortCode, "https://example.com/page", createdAt, null));
    }

    @Test
    void findByShortCodeReturnsEmptyWhenMissing() {
        assertThat(urlRepository.findByShortCode(uniqueCode())).isEmpty();
    }

    @Test
    void existsByShortCodeReflectsPersistedState() {
        String shortCode = uniqueCode();
        assertThat(urlRepository.existsByShortCode(shortCode)).isFalse();

        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com", Instant.now(), null));

        assertThat(urlRepository.existsByShortCode(shortCode)).isTrue();
    }

    @Test
    void persistsExpiresAt() {
        String shortCode = uniqueCode();
        Instant expiresAt = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS);

        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com", Instant.now(), expiresAt));

        assertThat(urlRepository.findByShortCode(shortCode)).get().extracting(ShortUrl::expiresAt).isEqualTo(expiresAt);
    }

    private String uniqueCode() {
        return "t" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
