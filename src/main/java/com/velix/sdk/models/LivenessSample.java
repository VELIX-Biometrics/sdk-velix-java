package com.velix.sdk.models;

/**
 * action in [center, move_closer, move_away] per spec enum.
 */
public record LivenessSample(String action, String imageBase64) {
    public LivenessSample {
        if (action == null || action.isBlank()) throw new IllegalArgumentException("action required");
        if (imageBase64 == null || imageBase64.isBlank()) throw new IllegalArgumentException("imageBase64 required");
    }
}
