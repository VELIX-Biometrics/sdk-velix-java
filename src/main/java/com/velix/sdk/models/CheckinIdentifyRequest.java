package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Contrato real de POST /v1/api/checkin/identify (scope checkin:write).
 * Espelha src/modules/checkin/dto/identify-face.dto.ts (IdentifyFaceDto).
 * Wire casing: campos já definidos em camelCase na spec (imageBase64, topK).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CheckinIdentifyRequest(
    String imageBase64,
    List<String> images,
    Integer topK,
    LivenessBlock liveness,
    Location location
) {
    public CheckinIdentifyRequest {
        if (imageBase64 == null || imageBase64.isBlank())
            throw new IllegalArgumentException("imageBase64 required");
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String imageBase64;
        private List<String> images;
        private Integer topK;
        private LivenessBlock liveness;
        private Location location;

        public Builder imageBase64(String v) { this.imageBase64 = v; return this; }
        public Builder images(List<String> v) { this.images = v; return this; }
        public Builder topK(Integer v) { this.topK = v; return this; }
        public Builder liveness(LivenessBlock v) { this.liveness = v; return this; }
        public Builder location(Location v) { this.location = v; return this; }

        public CheckinIdentifyRequest build() {
            return new CheckinIdentifyRequest(imageBase64, images, topK, liveness, location);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Location(Double latitude, Double longitude, Double accuracy) {}
}
