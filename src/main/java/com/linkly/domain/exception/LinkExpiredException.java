package com.linkly.domain.exception;

public class LinkExpiredException extends DomainException {

    public LinkExpiredException(String shortCode) {
        super("short code expired: " + shortCode);
    }
}
