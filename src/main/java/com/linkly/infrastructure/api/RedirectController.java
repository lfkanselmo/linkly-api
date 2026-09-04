package com.linkly.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.linkly.application.service.RedirectService;

@RestController
class RedirectController {

    private final RedirectService redirectService;

    RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{code}")
    ResponseEntity<Void> redirect(@PathVariable String code) {
        return ResponseEntity.status(302).location(redirectService.resolve(code)).build();
    }
}
