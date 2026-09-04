package com.linkly.application.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShortenRequest(
        @NotBlank String originalUrl,
        @Pattern(regexp = "^[A-Za-z0-9]{3,20}$") String customCode,
        Instant expiresAt) {
}
