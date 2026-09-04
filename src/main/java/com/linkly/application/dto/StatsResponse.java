package com.linkly.application.dto;

import java.util.List;

import com.linkly.domain.model.ClickCountByPeriod;
import com.linkly.domain.model.TopValue;

public record StatsResponse(
        long totalClicks,
        List<ClickCountByPeriod> series,
        List<TopValue> topBrowsers,
        List<TopValue> topOperatingSystems,
        List<TopValue> topCountries,
        List<TopValue> topReferrers) {
}
