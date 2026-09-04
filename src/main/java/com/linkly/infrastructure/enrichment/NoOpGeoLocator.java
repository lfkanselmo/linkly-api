package com.linkly.infrastructure.enrichment;

import org.springframework.stereotype.Component;

import com.linkly.domain.model.GeoLocation;
import com.linkly.domain.port.GeoLocator;

// Placeholder hasta tener una license key de MaxMind GeoLite2 (cuenta gratuita en maxmind.com).
// Reemplazar por un MaxmindGeoLocator que lea el .mmdb local vía com.maxmind.geoip2:geoip2,
// ya declarado en el pom.xml.
@Component
class NoOpGeoLocator implements GeoLocator {

    @Override
    public GeoLocation locate(String ipAddress) {
        return new GeoLocation(null, null);
    }
}
