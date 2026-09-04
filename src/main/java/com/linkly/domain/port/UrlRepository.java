package com.linkly.domain.port;

import java.util.Optional;

import com.linkly.domain.model.ShortUrl;

public interface UrlRepository {

    ShortUrl save(ShortUrl shortUrl);

    Optional<ShortUrl> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}
