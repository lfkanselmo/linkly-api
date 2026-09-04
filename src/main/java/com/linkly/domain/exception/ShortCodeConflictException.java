package com.linkly.domain.exception;

public class ShortCodeConflictException extends DomainException {

    public ShortCodeConflictException(String shortCode) {
        super("shortCode already in use: " + shortCode);
    }
}
