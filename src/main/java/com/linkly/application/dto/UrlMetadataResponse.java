package com.linkly.application.dto;

import java.time.Instant;

public record UrlMetadataResponse(
        String shortCode, String originalUrl, Instant createdAt, Instant expiresAt, long totalClicks) {
}
