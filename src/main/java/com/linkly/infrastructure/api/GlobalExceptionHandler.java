package com.linkly.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.linkly.domain.exception.DomainException;
import com.linkly.domain.exception.ShortCodeConflictException;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ShortCodeConflictException.class)
    ResponseEntity<String> handleConflict(ShortCodeConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
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
