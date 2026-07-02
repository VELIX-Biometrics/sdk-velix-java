package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeletionRequestResponse(
    @JsonProperty("protocol_number") String protocolNumber,
    String message
) {}
