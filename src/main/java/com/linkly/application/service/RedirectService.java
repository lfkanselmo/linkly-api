package com.linkly.application.service;

import java.net.URI;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.linkly.domain.exception.LinkExpiredException;
import com.linkly.domain.exception.LinkNotFoundException;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.UrlRepository;

@Service
public class RedirectService {

    private final UrlRepository urlRepository;

    public RedirectService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public URI resolve(String shortCode) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new LinkNotFoundException(shortCode));
        if (shortUrl.isExpired(Instant.now())) {
            throw new LinkExpiredException(shortCode);
        }
        return URI.create(shortUrl.originalUrl());
    }
}
