CREATE SEQUENCE short_url_seq START WITH 100000;

CREATE TABLE short_urls (
    id BIGINT PRIMARY KEY DEFAULT nextval('short_url_seq'),
    short_code VARCHAR(12) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at TIMESTAMPTZ
);

CREATE TABLE click_events (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(12) NOT NULL REFERENCES short_urls (short_code),
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ip_address INET NOT NULL,
    user_agent TEXT,
    referer TEXT,
    browser VARCHAR(50),
    operating_system VARCHAR(50),
    device_type VARCHAR(20),
    country VARCHAR(2),
    city VARCHAR(100)
);

CREATE INDEX idx_click_events_short_code_occurred_at ON click_events (short_code, occurred_at);
