package com.linkly.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface ShortUrlJpaRepository extends JpaRepository<ShortUrlEntity, Long> {

    Optional<ShortUrlEntity> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}
