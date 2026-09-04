package com.linkly.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.port.ClickPublisher;

@Component
class AsyncClickPublisher implements ClickPublisher {

    private final ApplicationEventPublisher eventPublisher;

    AsyncClickPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(ClickEvent event) {
        eventPublisher.publishEvent(event);
    }
}
