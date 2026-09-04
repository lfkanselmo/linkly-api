package com.linkly.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface ClickEventJpaRepository extends JpaRepository<ClickEventEntity, Long> {
}
