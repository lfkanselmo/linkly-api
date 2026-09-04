package com.linkly.infrastructure.api;

import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.linkly.domain.model.StatsPeriod;

@Component
class StatsPeriodConverter implements Converter<String, StatsPeriod> {

    @Override
    public StatsPeriod convert(String source) {
        return StatsPeriod.valueOf(source.toUpperCase(Locale.ROOT));
    }
}
