package com.velix.sdk.exceptions;

public class RateLimitException extends VelixException {
    private final long retryAfterSeconds;

    public RateLimitException(long retryAfterSeconds) {
        super("Rate limit exceeded. Retry after " + retryAfterSeconds + "s", 429);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() { return retryAfterSeconds; }
}
