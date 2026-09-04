package com.linkly.infrastructure.api;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.linkly.application.service.RedirectService;

@RestController
class RedirectController {

    private final RedirectService redirectService;

    RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{code}")
    ResponseEntity<Void> redirect(
            @PathVariable String code,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent,
            @RequestHeader(value = HttpHeaders.REFERER, required = false) String referer,
            HttpServletRequest request) {
        var target = redirectService.resolve(code, request.getRemoteAddr(), userAgent, referer);
        return ResponseEntity.status(302).location(target).build();
    }
}
