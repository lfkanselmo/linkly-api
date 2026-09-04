package com.linkly.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.linkly.domain.exception.DomainException;
import com.linkly.domain.exception.LinkExpiredException;
import com.linkly.domain.exception.LinkNotFoundException;
import com.linkly.domain.exception.ShortCodeConflictException;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ShortCodeConflictException.class)
    ResponseEntity<String> handleConflict(ShortCodeConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(LinkNotFoundException.class)
    ResponseEntity<String> handleNotFound(LinkNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(LinkExpiredException.class)
    ResponseEntity<String> handleExpired(LinkExpiredException exception) {
        return ResponseEntity.status(HttpStatus.GONE).body(exception.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    ResponseEntity<String> handleDomainException(DomainException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
