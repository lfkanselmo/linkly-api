package com.linkly.infrastructure.persistence;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "short_urls")
class ShortUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "short_url_seq")
    @SequenceGenerator(name = "short_url_seq", sequenceName = "short_url_seq", allocationSize = 1)
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    protected ShortUrlEntity() {
    }

    ShortUrlEntity(String shortCode, String originalUrl, Instant createdAt, Instant expiresAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    Long getId() {
        return id;
    }

    String getShortCode() {
        return shortCode;
    }

    String getOriginalUrl() {
        return originalUrl;
    }

    Instant getCreatedAt() {
        return createdAt;
    }

    Instant getExpiresAt() {
        return expiresAt;
    }
}
