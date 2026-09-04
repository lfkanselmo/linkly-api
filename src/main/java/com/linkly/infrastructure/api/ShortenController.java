package com.linkly.infrastructure.api;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkly.application.dto.ShortenRequest;
import com.linkly.application.dto.ShortenResponse;
import com.linkly.application.service.UrlCommandService;

@RestController
@RequestMapping("/api/v1/urls")
class ShortenController {

    private final UrlCommandService urlCommandService;

    ShortenController(UrlCommandService urlCommandService) {
        this.urlCommandService = urlCommandService;
    }

    @PostMapping
    ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlCommandService.shorten(request));
    }
}
