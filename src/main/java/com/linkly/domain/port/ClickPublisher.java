package com.linkly.domain.port;

import com.linkly.domain.model.ClickEvent;

public interface ClickPublisher {

    void publish(ClickEvent event);
}
