package com.linkly.domain.port;

import java.time.Instant;
import java.util.List;

import com.linkly.domain.model.ClickCountByPeriod;
import com.linkly.domain.model.StatsPeriod;
import com.linkly.domain.model.TopValue;

public interface ClickStatsRepository {

    long countByShortCode(String shortCode);

    List<ClickCountByPeriod> countByPeriod(String shortCode, Instant from, Instant to, StatsPeriod period);

    List<TopValue> topBrowsers(String shortCode, int limit);

    List<TopValue> topOperatingSystems(String shortCode, int limit);

    List<TopValue> topCountries(String shortCode, int limit);

    List<TopValue> topReferrers(String shortCode, int limit);
}
