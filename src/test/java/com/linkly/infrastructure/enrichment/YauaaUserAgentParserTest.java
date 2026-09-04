package com.linkly.infrastructure.enrichment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class YauaaUserAgentParserTest {

    private final YauaaUserAgentParser parser = new YauaaUserAgentParser();

    @Test
    void parsesAKnownDesktopChromeUserAgent() {
        String userAgent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/120.0.0.0 Safari/537.36";

        var result = parser.parse(userAgent);

        assertThat(result.browser()).isEqualTo("Chrome");
        assertThat(result.operatingSystem()).isEqualTo("Windows NT");
        assertThat(result.deviceType()).isEqualTo("Desktop");
    }

    @Test
    void returnsAllNullFieldsForBlankUserAgent() {
        var result = parser.parse(" ");

        assertThat(result.browser()).isNull();
        assertThat(result.operatingSystem()).isNull();
        assertThat(result.deviceType()).isNull();
    }
}
