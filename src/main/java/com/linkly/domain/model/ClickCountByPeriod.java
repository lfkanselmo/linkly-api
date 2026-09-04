package com.linkly.domain.model;

import java.time.Instant;

public record ClickCountByPeriod(Instant periodStart, long count) {
}
