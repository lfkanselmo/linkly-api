package com.linkly.infrastructure.api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkly.application.dto.StatsResponse;
import com.linkly.application.dto.UrlMetadataResponse;
import com.linkly.application.service.StatsQueryService;
import com.linkly.domain.model.StatsPeriod;

@RestController
@RequestMapping("/api/v1/urls")
class StatsController {

    private static final int DEFAULT_WINDOW_DAYS = 30;

    private final StatsQueryService statsQueryService;

    StatsController(StatsQueryService statsQueryService) {
        this.statsQueryService = statsQueryService;
    }

    @GetMapping("/{code}")
    UrlMetadataResponse metadata(@PathVariable String code) {
        return statsQueryService.metadata(code);
    }

    @GetMapping("/{code}/stats")
    StatsResponse stats(
            @PathVariable String code,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(name = "groupBy", defaultValue = "DAY") StatsPeriod period) {
        Instant effectiveTo = to != null ? to : Instant.now();
        Instant effectiveFrom = from != null ? from : effectiveTo.minus(DEFAULT_WINDOW_DAYS, ChronoUnit.DAYS);
        return statsQueryService.stats(code, effectiveFrom, effectiveTo, period);
    }
}
