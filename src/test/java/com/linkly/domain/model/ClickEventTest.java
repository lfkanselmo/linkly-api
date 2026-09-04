package com.linkly.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.linkly.domain.exception.InvalidClickEventException;

class ClickEventTest {

    @Test
    void createsWithMandatoryFieldsOnly() {
        ClickEvent event = new ClickEvent("aB3xQ", Instant.now(), "203.0.113.1", null, null, null, null, null, null, null);
        assertThat(event.shortCode()).isEqualTo("aB3xQ");
    }

    @Test
    void rejectsBlankShortCode() {
        assertThatThrownBy(() -> new ClickEvent(" ", Instant.now(), "203.0.113.1", null, null, null, null, null, null, null))
                .isInstanceOf(InvalidClickEventException.class);
    }

    @Test
    void rejectsMissingIpAddress() {
        assertThatThrownBy(() -> new ClickEvent("aB3xQ", Instant.now(), " ", null, null, null, null, null, null, null))
                .isInstanceOf(InvalidClickEventException.class);
    }

    @Test
    void rejectsMissingTimestamp() {
        assertThatThrownBy(() -> new ClickEvent("aB3xQ", null, "203.0.113.1", null, null, null, null, null, null, null))
                .isInstanceOf(InvalidClickEventException.class);
    }
}
