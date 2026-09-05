package com.linkly.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.linkly.infrastructure.api.RateLimitInterceptor;

@Configuration
class WebConfig implements WebMvcConfigurer {

    private final List<String> allowedOrigins;
    private final RateLimitInterceptor rateLimitInterceptor;

    WebConfig(@Value("${linkly.cors.allowed-origins}") List<String> allowedOrigins, RateLimitInterceptor rateLimitInterceptor) {
        this.allowedOrigins = allowedOrigins;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/v1/urls");
    }
}
