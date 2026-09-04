package com.linkly.application.service;

import java.time.Instant;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linkly.application.dto.ShortenRequest;
import com.linkly.application.dto.ShortenResponse;
import com.linkly.domain.exception.InvalidShortUrlException;
import com.linkly.domain.exception.ShortCodeConflictException;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.ShortCodeGenerator;
import com.linkly.domain.port.UrlRepository;

@Service
public class UrlCommandService {

    private static final Set<String> RESERVED_CODES = Set.of("api", "actuator", "swagger-ui", "v3", "favicon.ico");

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final String baseUrl;

    public UrlCommandService(
            UrlRepository urlRepository,
            ShortCodeGenerator shortCodeGenerator,
            @Value("${linkly.base-url}") String baseUrl) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.baseUrl = baseUrl;
    }

    public ShortenResponse shorten(ShortenRequest request) {
        rejectSelfReferentialUrl(request.originalUrl());
        String shortCode = resolveShortCode(request.customCode());
        ShortUrl shortUrl = new ShortUrl(0, shortCode, request.originalUrl(), Instant.now(), request.expiresAt());
        return toResponse(urlRepository.save(shortUrl));
    }

    private void rejectSelfReferentialUrl(String originalUrl) {
        if (originalUrl.toLowerCase(Locale.ROOT).startsWith(baseUrl.toLowerCase(Locale.ROOT))) {
            throw new InvalidShortUrlException("originalUrl must not point back to Linkly: " + originalUrl);
        }
    }

    private String resolveShortCode(String customCode) {
        if (customCode == null) {
            return shortCodeGenerator.generate();
        }
        if (RESERVED_CODES.contains(customCode.toLowerCase(Locale.ROOT))) {
            throw new InvalidShortUrlException("customCode is reserved: " + customCode);
        }
        if (urlRepository.existsByShortCode(customCode)) {
            throw new ShortCodeConflictException(customCode);
        }
        return customCode;
    }

    private ShortenResponse toResponse(ShortUrl shortUrl) {
        String fullShortUrl = baseUrl + "/" + shortUrl.shortCode();
        return new ShortenResponse(
                shortUrl.shortCode(), fullShortUrl, shortUrl.originalUrl(), shortUrl.createdAt(), shortUrl.expiresAt());
    }
}
