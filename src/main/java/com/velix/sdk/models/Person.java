package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Person(
    String id,
    String name,
    String email,
    @JsonProperty("external_id") String externalId,
    @JsonProperty("has_biometric") boolean hasBiometric,
    String status,
    @JsonProperty("created_at") String createdAt
) {}
