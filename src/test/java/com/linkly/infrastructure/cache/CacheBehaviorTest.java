package com.linkly.infrastructure.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class CacheBehaviorTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void servesCachedLookupsAfterTheUnderlyingRowIsGone() {
        String shortCode = "cache" + UUID.randomUUID().toString().substring(0, 6);
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com/cached", Instant.now(), null));

        assertThat(urlRepository.findByShortCode(shortCode)).isPresent();

        jdbcTemplate.update("DELETE FROM short_urls WHERE short_code = ?", shortCode);

        assertThat(urlRepository.findByShortCode(shortCode)).isPresent();
    }
}
