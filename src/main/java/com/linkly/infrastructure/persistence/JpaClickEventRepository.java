package com.linkly.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.port.ClickEventRepository;

@Repository
class JpaClickEventRepository implements ClickEventRepository {

    private final ClickEventJpaRepository jpaRepository;

    JpaClickEventRepository(ClickEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ClickEvent save(ClickEvent event) {
        ClickEventEntity entity = new ClickEventEntity(
                event.shortCode(), event.occurredAt(), event.ipAddress(), event.userAgent(), event.referer(),
                event.browser(), event.operatingSystem(), event.deviceType(), event.country(), event.city());
        jpaRepository.save(entity);
        return event;
    }
}
