package com.linkly.infrastructure.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void redirectsToOriginalUrl() throws Exception {
        String shortCode = uniqueCode();
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com/target", Instant.now(), null));

        mockMvc.perform(get("/{code}", shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com/target"));
    }

    @Test
    void returnsNotFoundForUnknownCode() throws Exception {
        mockMvc.perform(get("/{code}", uniqueCode())).andExpect(status().isNotFound());
    }

    @Test
    void returnsGoneForExpiredCode() throws Exception {
        String shortCode = uniqueCode();
        Instant expiresAt = Instant.now().minus(1, ChronoUnit.DAYS);
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com/expired", Instant.now(), expiresAt));

        mockMvc.perform(get("/{code}", shortCode)).andExpect(status().isGone());
    }

    private String uniqueCode() {
        return "r" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
