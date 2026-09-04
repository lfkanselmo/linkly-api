package com.linkly.domain.model;

import java.time.Instant;

import com.linkly.domain.exception.InvalidClickEventException;

public record ClickEvent(
        String shortCode,
        Instant occurredAt,
        String ipAddress,
        String userAgent,
        String referer,
        String browser,
        String operatingSystem,
        String deviceType,
        String country,
        String city) {

    public ClickEvent {
        if (shortCode == null || shortCode.isBlank()) {
            throw new InvalidClickEventException("shortCode must not be blank");
        }
        if (occurredAt == null) {
            throw new InvalidClickEventException("occurredAt must not be null");
        }
        if (ipAddress == null || ipAddress.isBlank()) {
            throw new InvalidClickEventException("ipAddress must not be blank");
        }
    }
}
