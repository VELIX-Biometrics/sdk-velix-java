package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Conteúdo de Envelope.data para POST /v1/api/checkin/identify.
 * O único indicador de liveness exposto publicamente é `matched`/`quality_score` —
 * score de liveness NUNCA é retornado pela API (regra de segurança global VELIX).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CheckinIdentifyResponse(
    boolean matched,
    @JsonProperty("person_id") String personId,
    @JsonProperty("quality_score") double qualityScore,
    String message
) {}
