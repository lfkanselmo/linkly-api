package com.linkly.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class MetricsLoggingAspect {

    private final MeterRegistry meterRegistry;

    MetricsLoggingAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(public * com.linkly.application.service..*(..))")
    Object recordExecutionMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return joinPoint.proceed();
        } finally {
            sample.stop(Timer.builder("linkly.service.execution")
                    .tag("method", joinPoint.getSignature().toShortString())
                    .register(meterRegistry));
        }
    }
}
