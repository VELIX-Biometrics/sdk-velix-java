package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CheckinResult(
    boolean passed,
    @JsonProperty("person_id") String personId,
    String name,
    @JsonProperty("access_log_id") String accessLogId
) {}
