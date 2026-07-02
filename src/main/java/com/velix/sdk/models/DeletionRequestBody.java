package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Body de POST /v1/api/deletion-request (scope lgpd:write).
 * A pessoa deve ter uma Identity ativa vinculada ao tenant dono da API key —
 * caso contrário a API responde 403.
 */
public record DeletionRequestBody(@JsonProperty("person_id") String personId) {
    public DeletionRequestBody {
        if (personId == null || personId.isBlank()) throw new IllegalArgumentException("personId required");
    }
}
