package com.linkly.domain.exception;

public class LinkNotFoundException extends DomainException {

    public LinkNotFoundException(String shortCode) {
        super("short code not found: " + shortCode);
    }
}
