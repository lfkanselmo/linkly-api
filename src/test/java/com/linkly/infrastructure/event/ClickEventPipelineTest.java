package com.linkly.infrastructure.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.GeoLocation;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.GeoLocator;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ClickEventPipelineTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean
    private GeoLocator geoLocator;

    @Test
    void redirectRespondsBeforeSlowEnrichmentCompletes() throws Exception {
        String shortCode = uniqueCode();
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com/slow-enrichment", Instant.now(), null));
        when(geoLocator.locate(anyString())).thenAnswer(invocation -> {
            Thread.sleep(2000);
            return new GeoLocation(null, null);
        });

        Instant start = Instant.now();
        mockMvc.perform(get("/{code}", shortCode)).andExpect(status().isFound());
        Duration elapsed = Duration.between(start, Instant.now());

        assertThat(elapsed).isLessThan(Duration.ofMillis(500));
    }

    @Test
    void clickGetsPersistedAsynchronouslyAfterRedirect() throws Exception {
        String shortCode = uniqueCode();
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com/tracked", Instant.now(), null));
        when(geoLocator.locate(anyString())).thenReturn(new GeoLocation(null, null));

        mockMvc.perform(get("/{code}", shortCode)).andExpect(status().isFound());

        awaitClickPersisted(shortCode);
    }

    private void awaitClickPersisted(String shortCode) throws InterruptedException {
        Instant deadline = Instant.now().plusSeconds(5);
        while (Instant.now().isBefore(deadline)) {
            Long count = jdbcTemplate.queryForObject(
                    "SELECT count(*) FROM click_events WHERE short_code = ?", Long.class, shortCode);
            if (count != null && count > 0) {
                return;
            }
            Thread.sleep(50);
        }
        throw new AssertionError("click was not persisted within the timeout for " + shortCode);
    }

    private String uniqueCode() {
        return "e" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
