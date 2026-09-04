package com.linkly.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.linkly.application.dto.StatsResponse;
import com.linkly.application.dto.UrlMetadataResponse;
import com.linkly.domain.exception.LinkNotFoundException;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.model.StatsPeriod;
import com.linkly.domain.port.ClickStatsRepository;
import com.linkly.domain.port.UrlRepository;

@Service
public class StatsQueryService {

    private static final int TOP_N = 5;

    private final UrlRepository urlRepository;
    private final ClickStatsRepository clickStatsRepository;

    public StatsQueryService(UrlRepository urlRepository, ClickStatsRepository clickStatsRepository) {
        this.urlRepository = urlRepository;
        this.clickStatsRepository = clickStatsRepository;
    }

    public UrlMetadataResponse metadata(String shortCode) {
        ShortUrl shortUrl = findOrThrow(shortCode);
        long totalClicks = clickStatsRepository.countByShortCode(shortCode);
        return new UrlMetadataResponse(
                shortUrl.shortCode(), shortUrl.originalUrl(), shortUrl.createdAt(), shortUrl.expiresAt(), totalClicks);
    }

    public StatsResponse stats(String shortCode, Instant from, Instant to, StatsPeriod period) {
        findOrThrow(shortCode);
        return new StatsResponse(
                clickStatsRepository.countByShortCode(shortCode),
                clickStatsRepository.countByPeriod(shortCode, from, to, period),
                clickStatsRepository.topBrowsers(shortCode, TOP_N),
                clickStatsRepository.topOperatingSystems(shortCode, TOP_N),
                clickStatsRepository.topCountries(shortCode, TOP_N),
                clickStatsRepository.topReferrers(shortCode, TOP_N));
    }

    private ShortUrl findOrThrow(String shortCode) {
        return urlRepository.findByShortCode(shortCode).orElseThrow(() -> new LinkNotFoundException(shortCode));
    }
}
