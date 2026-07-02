package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Contrato real de POST /v1/api/onboarding (scope onboarding:write).
 * Espelha src/modules/onboarding/dto/onboarding.dto.ts (OnboardingDto) do
 * api-velix-identity-core. Wire casing: snake_case.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OnboardingRequest(
    String name,
    List<String> frames,
    String email,
    String phone,
    String document,
    @JsonProperty("document_type") String documentType,
    @JsonProperty("external_id") String externalId,
    Map<String, Object> metadata,
    String role,
    @JsonProperty("access_groups") List<String> accessGroups
) {
    public OnboardingRequest {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (frames == null || frames.isEmpty()) throw new IllegalArgumentException("frames required (minItems 1)");
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String name;
        private List<String> frames;
        private String email;
        private String phone;
        private String document;
        private String documentType;
        private String externalId;
        private Map<String, Object> metadata;
        private String role;
        private List<String> accessGroups;

        public Builder name(String v) { this.name = v; return this; }
        public Builder frames(List<String> v) { this.frames = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder phone(String v) { this.phone = v; return this; }
        public Builder document(String v) { this.document = v; return this; }
        public Builder documentType(String v) { this.documentType = v; return this; }
        public Builder externalId(String v) { this.externalId = v; return this; }
        public Builder metadata(Map<String, Object> v) { this.metadata = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public Builder accessGroups(List<String> v) { this.accessGroups = v; return this; }

        public OnboardingRequest build() {
            return new OnboardingRequest(name, frames, email, phone, document, documentType,
                externalId, metadata, role, accessGroups);
        }
    }
}
