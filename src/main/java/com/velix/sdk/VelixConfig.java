package com.velix.sdk;

import java.time.Duration;

public record VelixConfig(
    String apiUrl,
    String apiKey,
    Duration timeout,
    int maxRetries
) {
    public VelixConfig {
        if (apiUrl == null || apiUrl.isBlank()) throw new IllegalArgumentException("apiUrl required");
        if (apiKey == null || apiKey.isBlank()) throw new IllegalArgumentException("apiKey required");
        if (timeout == null) timeout = Duration.ofSeconds(30);
        if (maxRetries < 0) maxRetries = 3;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String apiUrl;
        private String apiKey;
        private Duration timeout = Duration.ofSeconds(30);
        private int maxRetries = 3;

        public Builder apiUrl(String v)    { this.apiUrl = v; return this; }
        public Builder apiKey(String v)    { this.apiKey = v; return this; }
        public Builder timeout(Duration v) { this.timeout = v; return this; }
        public Builder maxRetries(int v)   { this.maxRetries = v; return this; }

        public VelixConfig build() { return new VelixConfig(apiUrl, apiKey, timeout, maxRetries); }
    }
}
