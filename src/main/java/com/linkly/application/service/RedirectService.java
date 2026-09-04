package com.linkly.application.service;

import java.net.URI;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.linkly.domain.exception.LinkExpiredException;
import com.linkly.domain.exception.LinkNotFoundException;
import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.ClickPublisher;
import com.linkly.domain.port.UrlRepository;

@Service
public class RedirectService {

    private final UrlRepository urlRepository;
    private final ClickPublisher clickPublisher;

    public RedirectService(UrlRepository urlRepository, ClickPublisher clickPublisher) {
        this.urlRepository = urlRepository;
        this.clickPublisher = clickPublisher;
    }

    public URI resolve(String shortCode, String ipAddress, String userAgent, String referer) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new LinkNotFoundException(shortCode));
        if (shortUrl.isExpired(Instant.now())) {
            throw new LinkExpiredException(shortCode);
        }
        clickPublisher.publish(
                new ClickEvent(shortCode, Instant.now(), ipAddress, userAgent, referer, null, null, null, null, null));
        return URI.create(shortUrl.originalUrl());
    }
}
