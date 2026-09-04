package com.linkly.domain.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

import com.linkly.domain.exception.InvalidShortUrlException;

public record ShortUrl(long id, String shortCode, String originalUrl, Instant createdAt, Instant expiresAt) {

    private static final int MAX_URL_LENGTH = 2048;

    public ShortUrl {
        if (shortCode == null || shortCode.isBlank()) {
            throw new InvalidShortUrlException("shortCode must not be blank");
        }
        validateOriginalUrl(originalUrl);
        if (createdAt == null) {
            throw new InvalidShortUrlException("createdAt must not be null");
        }
    }

    public boolean isExpired(Instant now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }

    private static void validateOriginalUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.length() > MAX_URL_LENGTH) {
            throw new InvalidShortUrlException("originalUrl must be a valid http(s) URL: " + originalUrl);
        }
        String scheme = parseScheme(originalUrl);
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new InvalidShortUrlException("originalUrl must be a valid http(s) URL: " + originalUrl);
        }
    }

    private static String parseScheme(String originalUrl) {
        try {
            return new URI(originalUrl).getScheme();
        } catch (URISyntaxException exception) {
            throw new InvalidShortUrlException("originalUrl must be a valid http(s) URL: " + originalUrl);
        }
    }
}
