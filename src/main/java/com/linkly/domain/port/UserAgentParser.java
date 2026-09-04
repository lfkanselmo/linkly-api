package com.linkly.domain.port;

import com.linkly.domain.model.UserAgentInfo;

public interface UserAgentParser {

    UserAgentInfo parse(String userAgent);
}
