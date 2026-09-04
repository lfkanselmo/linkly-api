package com.linkly.infrastructure.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.ClickEventRepository;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ClickEventRepository clickEventRepository;

    @Test
    void returnsMetadataWithTotalClicks() throws Exception {
        String shortCode = createShortUrl();
        clickEventRepository.save(rawClick(shortCode));

        mockMvc.perform(get("/api/v1/urls/{code}", shortCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value(shortCode))
                .andExpect(jsonPath("$.totalClicks").value(1));
    }

    @Test
    void returnsNotFoundForUnknownCodeMetadata() throws Exception {
        mockMvc.perform(get("/api/v1/urls/{code}", uniqueCode())).andExpect(status().isNotFound());
    }

    @Test
    void returnsStatsWithSeriesAndTopBrowsers() throws Exception {
        String shortCode = createShortUrl();
        clickEventRepository.save(rawClick(shortCode));

        mockMvc.perform(get("/api/v1/urls/{code}/stats?groupBy=day", shortCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClicks").value(1))
                .andExpect(jsonPath("$.series").isArray())
                .andExpect(jsonPath("$.topBrowsers[0].value").value("Chrome"));
    }

    private ClickEvent rawClick(String shortCode) {
        return new ClickEvent(
                shortCode, Instant.now(), "203.0.113.1", "test-agent", null, "Chrome", "Windows", "Desktop", "CO", "Bogota");
    }

    private String createShortUrl() {
        String shortCode = uniqueCode();
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com", Instant.now(), null));
        return shortCode;
    }

    private String uniqueCode() {
        return "m" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
