package com.linkly.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.linkly.TestcontainersConfiguration;
import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.model.ClickCountByPeriod;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.model.StatsPeriod;
import com.linkly.domain.model.TopValue;
import com.linkly.domain.port.ClickEventRepository;
import com.linkly.domain.port.ClickStatsRepository;
import com.linkly.domain.port.UrlRepository;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class JdbcClickStatsRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ClickEventRepository clickEventRepository;

    @Autowired
    private ClickStatsRepository clickStatsRepository;

    @Test
    void countsTotalClicksForShortCode() {
        String shortCode = createShortUrl();
        saveClick(shortCode, "Chrome", "Windows", "CO", "https://ref.example");
        saveClick(shortCode, "Firefox", "Linux", "AR", null);

        assertThat(clickStatsRepository.countByShortCode(shortCode)).isEqualTo(2);
    }

    @Test
    void groupsClicksByDay() {
        String shortCode = createShortUrl();
        Instant now = Instant.now();
        saveClick(shortCode, "Chrome", "Windows", "CO", null);

        List<ClickCountByPeriod> series = clickStatsRepository.countByPeriod(
                shortCode, now.minus(1, ChronoUnit.DAYS), now.plus(1, ChronoUnit.DAYS), StatsPeriod.DAY);

        assertThat(series).hasSize(1);
        assertThat(series.get(0).count()).isEqualTo(1);
    }

    @Test
    void ranksTopBrowsersByClickCount() {
        String shortCode = createShortUrl();
        saveClick(shortCode, "Chrome", "Windows", "CO", null);
        saveClick(shortCode, "Chrome", "Windows", "CO", null);
        saveClick(shortCode, "Firefox", "Linux", "AR", null);

        List<TopValue> topBrowsers = clickStatsRepository.topBrowsers(shortCode, 5);

        assertThat(topBrowsers.get(0).value()).isEqualTo("Chrome");
        assertThat(topBrowsers.get(0).count()).isEqualTo(2);
    }

    @Test
    void excludesNullValuesFromTopReferrers() {
        String shortCode = createShortUrl();
        saveClick(shortCode, "Chrome", "Windows", "CO", null);
        saveClick(shortCode, "Chrome", "Windows", "CO", "https://ref.example");

        assertThat(clickStatsRepository.topReferrers(shortCode, 5)).hasSize(1);
    }

    private void saveClick(String shortCode, String browser, String os, String country, String referer) {
        clickEventRepository.save(new ClickEvent(
                shortCode, Instant.now(), "203.0.113.1", "test-agent", referer, browser, os, "Desktop", country, "Bogota"));
    }

    private String createShortUrl() {
        String shortCode = "s" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        urlRepository.save(new ShortUrl(0, shortCode, "https://example.com", Instant.now(), null));
        return shortCode;
    }
}
