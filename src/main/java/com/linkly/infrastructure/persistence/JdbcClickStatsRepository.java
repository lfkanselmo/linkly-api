package com.linkly.infrastructure.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.linkly.domain.model.ClickCountByPeriod;
import com.linkly.domain.model.StatsPeriod;
import com.linkly.domain.model.TopValue;
import com.linkly.domain.port.ClickStatsRepository;

@Repository
class JdbcClickStatsRepository implements ClickStatsRepository {

    private final JdbcTemplate jdbcTemplate;

    JdbcClickStatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long countByShortCode(String shortCode) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM click_events WHERE short_code = ?", Long.class, shortCode);
        return count == null ? 0 : count;
    }

    @Override
    public List<ClickCountByPeriod> countByPeriod(String shortCode, Instant from, Instant to, StatsPeriod period) {
        String sql = "SELECT date_trunc(?, occurred_at) AS bucket, count(*) AS total FROM click_events "
                + "WHERE short_code = ? AND occurred_at BETWEEN ? AND ? GROUP BY bucket ORDER BY bucket";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ClickCountByPeriod(rs.getTimestamp("bucket").toInstant(), rs.getLong("total")),
                sqlUnit(period), shortCode, Timestamp.from(from), Timestamp.from(to));
    }

    @Override
    public List<TopValue> topBrowsers(String shortCode, int limit) {
        return topValues("browser", shortCode, limit);
    }

    @Override
    public List<TopValue> topOperatingSystems(String shortCode, int limit) {
        return topValues("operating_system", shortCode, limit);
    }

    @Override
    public List<TopValue> topCountries(String shortCode, int limit) {
        return topValues("country", shortCode, limit);
    }

    @Override
    public List<TopValue> topReferrers(String shortCode, int limit) {
        return topValues("referer", shortCode, limit);
    }

    private List<TopValue> topValues(String column, String shortCode, int limit) {
        String sql = "SELECT " + column + " AS value, count(*) AS total FROM click_events "
                + "WHERE short_code = ? AND " + column + " IS NOT NULL "
                + "GROUP BY " + column + " ORDER BY total DESC LIMIT ?";
        return jdbcTemplate.query(
                sql, (rs, rowNum) -> new TopValue(rs.getString("value"), rs.getLong("total")), shortCode, limit);
    }

    private static String sqlUnit(StatsPeriod period) {
        return switch (period) {
            case DAY -> "day";
            case WEEK -> "week";
            case MONTH -> "month";
        };
    }
}
