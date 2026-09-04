package com.linkly.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.linkly.domain.model.ClickEvent;
import com.linkly.domain.model.GeoLocation;
import com.linkly.domain.model.UserAgentInfo;
import com.linkly.domain.port.ClickEventRepository;
import com.linkly.domain.port.GeoLocator;
import com.linkly.domain.port.UserAgentParser;

@Component
class ClickEventListener {

    private final UserAgentParser userAgentParser;
    private final GeoLocator geoLocator;
    private final ClickEventRepository clickEventRepository;

    ClickEventListener(UserAgentParser userAgentParser, GeoLocator geoLocator, ClickEventRepository clickEventRepository) {
        this.userAgentParser = userAgentParser;
        this.geoLocator = geoLocator;
        this.clickEventRepository = clickEventRepository;
    }

    @Async
    @EventListener
    void onClick(ClickEvent rawEvent) {
        UserAgentInfo agentInfo = userAgentParser.parse(rawEvent.userAgent());
        GeoLocation location = geoLocator.locate(rawEvent.ipAddress());
        clickEventRepository.save(enrich(rawEvent, agentInfo, location));
    }

    private ClickEvent enrich(ClickEvent raw, UserAgentInfo agentInfo, GeoLocation location) {
        return new ClickEvent(
                raw.shortCode(), raw.occurredAt(), raw.ipAddress(), raw.userAgent(), raw.referer(),
                agentInfo.browser(), agentInfo.operatingSystem(), agentInfo.deviceType(),
                location.country(), location.city());
    }
}
