package com.linkly.domain.port;

import com.linkly.domain.model.ClickEvent;

public interface ClickEventRepository {

    ClickEvent save(ClickEvent event);
}
