package com.linkly.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

class RateLimitInterceptorTest {

    @Test
    void blocksRequestsAfterTheConfiguredCapacity() {
        RateLimitInterceptor interceptor = new RateLimitInterceptor(2, 60);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRemoteAddr()).thenReturn("203.0.113.1");

        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
        assertThat(interceptor.preHandle(request, response, new Object())).isFalse();

        verify(response).setStatus(429);
    }

    @Test
    void doesNotConsumeCapacityForCorsPreflightRequests() {
        RateLimitInterceptor interceptor = new RateLimitInterceptor(1, 60);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRemoteAddr()).thenReturn("203.0.113.1");
        when(request.getMethod()).thenReturn("OPTIONS");

        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();

        when(request.getMethod()).thenReturn("POST");
        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
    }

    @Test
    void tracksLimitsPerRemoteAddressIndependently() {
        RateLimitInterceptor interceptor = new RateLimitInterceptor(1, 60);
        HttpServletRequest requestA = mock(HttpServletRequest.class);
        HttpServletRequest requestB = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(requestA.getRemoteAddr()).thenReturn("203.0.113.1");
        when(requestB.getRemoteAddr()).thenReturn("203.0.113.2");

        assertThat(interceptor.preHandle(requestA, response, new Object())).isTrue();
        assertThat(interceptor.preHandle(requestB, response, new Object())).isTrue();
    }
}
