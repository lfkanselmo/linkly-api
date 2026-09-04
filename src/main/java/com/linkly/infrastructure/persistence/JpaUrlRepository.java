package com.linkly.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.linkly.domain.model.ShortUrl;
import com.linkly.domain.port.UrlRepository;

@Repository
class JpaUrlRepository implements UrlRepository {

    private final ShortUrlJpaRepository jpaRepository;

    JpaUrlRepository(ShortUrlJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        ShortUrlEntity entity = new ShortUrlEntity(
                shortUrl.shortCode(), shortUrl.originalUrl(), shortUrl.createdAt(), shortUrl.expiresAt());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<ShortUrl> findByShortCode(String shortCode) {
        return jpaRepository.findByShortCode(shortCode).map(this::toDomain);
    }

    @Override
    public boolean existsByShortCode(String shortCode) {
        return jpaRepository.existsByShortCode(shortCode);
    }

    private ShortUrl toDomain(ShortUrlEntity entity) {
        return new ShortUrl(
                entity.getId(), entity.getShortCode(), entity.getOriginalUrl(), entity.getCreatedAt(), entity.getExpiresAt());
    }
}
