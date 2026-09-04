package com.linkly.application.dto;

import java.time.Instant;

public record ShortenResponse(
        String shortCode, String shortUrl, String originalUrl, Instant createdAt, Instant expiresAt) {
}
