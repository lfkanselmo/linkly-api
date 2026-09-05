package com.linkly.infrastructure.api;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final int capacity;
    private final Duration refillPeriod;
    private final Cache<String, Bucket> buckets =
            Caffeine.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).build();

    RateLimitInterceptor(
            @Value("${linkly.rate-limit.capacity}") int capacity,
            @Value("${linkly.rate-limit.refill-period-seconds}") long refillPeriodSeconds) {
        this.capacity = capacity;
        this.refillPeriod = Duration.ofSeconds(refillPeriodSeconds);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Bucket bucket = buckets.get(request.getRemoteAddr(), key -> newBucket());
        if (bucket.tryConsume(1)) {
            return true;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.builder().capacity(capacity).refillGreedy(capacity, refillPeriod).build();
        return Bucket.builder().addLimit(limit).build();
    }
}
