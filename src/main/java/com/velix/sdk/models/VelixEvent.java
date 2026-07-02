package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VelixEvent(
    String id,
    String name,
    String status,
    @JsonProperty("tenant_id") String tenantId,
    @JsonProperty("start_date") String startDate,
    @JsonProperty("end_date") String endDate,
    @JsonProperty("checkin_count") int checkinCount,
    @JsonProperty("created_at") String createdAt
) {}
