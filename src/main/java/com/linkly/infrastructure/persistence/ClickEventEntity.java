package com.linkly.infrastructure.persistence;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "click_events")
class ClickEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false)
    private String shortCode;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "referer")
    private String referer;

    @Column(name = "browser")
    private String browser;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    protected ClickEventEntity() {
    }

    ClickEventEntity(
            String shortCode,
            Instant occurredAt,
            String ipAddress,
            String userAgent,
            String referer,
            String browser,
            String operatingSystem,
            String deviceType,
            String country,
            String city) {
        this.shortCode = shortCode;
        this.occurredAt = occurredAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referer = referer;
        this.browser = browser;
        this.operatingSystem = operatingSystem;
        this.deviceType = deviceType;
        this.country = country;
        this.city = city;
    }

    String getShortCode() {
        return shortCode;
    }

    Instant getOccurredAt() {
        return occurredAt;
    }

    String getIpAddress() {
        return ipAddress;
    }

    String getUserAgent() {
        return userAgent;
    }

    String getReferer() {
        return referer;
    }

    String getBrowser() {
        return browser;
    }

    String getOperatingSystem() {
        return operatingSystem;
    }

    String getDeviceType() {
        return deviceType;
    }

    String getCountry() {
        return country;
    }

    String getCity() {
        return city;
    }
}
