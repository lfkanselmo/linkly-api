package com.linkly.domain.model;

import java.time.Instant;
import java.util.regex.Pattern;

import com.linkly.domain.exception.InvalidShortUrlException;

public record ShortUrl(long id, String shortCode, String originalUrl, Instant createdAt, Instant expiresAt) {

    private static final Pattern HTTP_URL = Pattern.compile("^https?://.+");
    private static final int MAX_URL_LENGTH = 2048;

    public ShortUrl {
        if (shortCode == null || shortCode.isBlank()) {
            throw new InvalidShortUrlException("shortCode must not be blank");
        }
        if (originalUrl == null || originalUrl.length() > MAX_URL_LENGTH || !HTTP_URL.matcher(originalUrl).matches()) {
            throw new InvalidShortUrlException("originalUrl must be a valid http(s) URL: " + originalUrl);
        }
        if (createdAt == null) {
            throw new InvalidShortUrlException("createdAt must not be null");
        }
    }

    public boolean isExpired(Instant now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }
}
