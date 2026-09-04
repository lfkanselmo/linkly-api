package com.linkly.infrastructure.generator;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.linkly.domain.codec.Base62Codec;
import com.linkly.domain.codec.SequencePermutation;
import com.linkly.domain.port.ShortCodeGenerator;

@Component
class SequenceShortCodeGenerator implements ShortCodeGenerator {

    private final JdbcTemplate jdbcTemplate;

    SequenceShortCodeGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String generate() {
        Long sequenceValue = jdbcTemplate.queryForObject("SELECT nextval('short_code_seq')", Long.class);
        return Base62Codec.encode(SequencePermutation.permute(sequenceValue));
    }
}
