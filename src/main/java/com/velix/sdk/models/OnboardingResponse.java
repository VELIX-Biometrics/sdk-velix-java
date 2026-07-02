package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Conteúdo de Envelope.data para POST /v1/api/onboarding. Wire casing: snake_case.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OnboardingResponse(
    @JsonProperty("person_id") String personId,
    @JsonProperty("identity_id") String identityId,
    boolean enrolled,
    @JsonProperty("frames_processed") int framesProcessed,
    @JsonProperty("frames_results") List<FrameResult> framesResults,
    @JsonProperty("embedding_id") String embeddingId,
    String message
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FrameResult(
        @JsonProperty("frame_index") int frameIndex,
        @JsonProperty("quality_passed") boolean qualityPassed,
        @JsonProperty("quality_score") double qualityScore,
        @JsonProperty("liveness_passed") boolean livenessPassed
    ) {}
}
