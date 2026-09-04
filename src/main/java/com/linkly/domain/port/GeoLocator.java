package com.linkly.domain.port;

import com.linkly.domain.model.GeoLocation;

public interface GeoLocator {

    GeoLocation locate(String ipAddress);
}
