package com.linkly.infrastructure.enrichment;

import org.springframework.stereotype.Component;

import com.linkly.domain.model.UserAgentInfo;
import com.linkly.domain.port.UserAgentParser;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

@Component
class YauaaUserAgentParser implements UserAgentParser {

    private final UserAgentAnalyzer analyzer = UserAgentAnalyzer.newBuilder()
            .hideMatcherLoadStats()
            .withField("AgentName")
            .withField("OperatingSystemName")
            .withField("DeviceClass")
            .withCache(1000)
            .build();

    @Override
    public UserAgentInfo parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return new UserAgentInfo(null, null, null);
        }
        UserAgent parsed = analyzer.parse(userAgent);
        return new UserAgentInfo(
                parsed.getValue("AgentName"), parsed.getValue("OperatingSystemName"), parsed.getValue("DeviceClass"));
    }
}
