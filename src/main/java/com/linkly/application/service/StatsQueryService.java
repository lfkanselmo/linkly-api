package com.linkly.application.service;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var totalClicks = executor.submit(() -> clickStatsRepository.countByShortCode(shortCode));
            var series = executor.submit(() -> clickStatsRepository.countByPeriod(shortCode, from, to, period));
            var topBrowsers = executor.submit(() -> clickStatsRepository.topBrowsers(shortCode, TOP_N));
            var topOperatingSystems = executor.submit(() -> clickStatsRepository.topOperatingSystems(shortCode, TOP_N));
            var topCountries = executor.submit(() -> clickStatsRepository.topCountries(shortCode, TOP_N));
            var topReferrers = executor.submit(() -> clickStatsRepository.topReferrers(shortCode, TOP_N));
            return new StatsResponse(
                    get(totalClicks), get(series), get(topBrowsers), get(topOperatingSystems), get(topCountries), get(topReferrers));
        }
    }

    private ShortUrl findOrThrow(String shortCode) {
        return urlRepository.findByShortCode(shortCode).orElseThrow(() -> new LinkNotFoundException(shortCode));
    }

    private static <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException(exception.getCause());
        }
    }
}
