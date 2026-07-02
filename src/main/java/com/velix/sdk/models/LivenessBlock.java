package com.velix.sdk.models;

import java.util.List;

/**
 * token = nonce obtido em GET /v1/public/checkin/{tenantSlug}/liveness/challenge
 * (endpoint público, sem API key — compartilhado entre os dois fluxos).
 */
public record LivenessBlock(String token, List<LivenessSample> samples) {
    public LivenessBlock {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token required");
        if (samples == null || samples.isEmpty()) throw new IllegalArgumentException("samples required");
    }
}
