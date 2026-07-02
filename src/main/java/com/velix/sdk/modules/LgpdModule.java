package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.DeletionRequestBody;
import com.velix.sdk.models.DeletionRequestResponse;

/**
 * POST /v1/api/deletion-request — scope lgpd:write (Velix.ID).
 * Equivalente server-to-server de POST /v1/me/deletion-request (portal
 * pessoal, JWT), recebendo person_id explícito.
 */
public final class LgpdModule {

    private final VelixClient client;

    public LgpdModule(VelixClient client) { this.client = client; }

    public DeletionRequestResponse requestDeletion(String personId) {
        return client.post("/v1/api/deletion-request", new DeletionRequestBody(personId),
            DeletionRequestResponse.class);
    }
}
