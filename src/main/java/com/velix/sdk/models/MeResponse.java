package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Conteúdo de Envelope.data para GET /v1/api/me/{personId} (scope me:read).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MeResponse(
    String id,
    String name,
    String email,
    String phone,
    @JsonProperty("photo_url") String photoUrl,
    @JsonProperty("created_at") String createdAt
) {}
