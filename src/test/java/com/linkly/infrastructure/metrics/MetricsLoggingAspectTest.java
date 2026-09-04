package com.linkly.infrastructure.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import io.micrometer.core.instrument.MeterRegistry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.linkly.TestcontainersConfiguration;
import com.linkly.application.dto.ShortenRequest;
import com.linkly.application.service.UrlCommandService;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class MetricsLoggingAspectTest {

    @Autowired
    private UrlCommandService urlCommandService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void recordsATaggedTimerForEveryApplicationServiceCall() {
        urlCommandService.shorten(new ShortenRequest("https://example.com/metrics-" + UUID.randomUUID(), null, null));

        var timers = meterRegistry.get("linkly.service.execution").timers();

        assertThat(timers).isNotEmpty();
        assertThat(timers).allSatisfy(timer -> assertThat(timer.getId().getTag("method")).isNotBlank());
    }
}
