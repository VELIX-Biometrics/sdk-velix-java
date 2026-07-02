package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.MeResponse;

/**
 * GET /v1/api/me/{personId} — scope me:read (Velix.ID).
 * Equivalente server-to-server de GET /v1/me (portal pessoal), por
 * personId explícito.
 */
public final class MeModule {

    private final VelixClient client;

    public MeModule(VelixClient client) { this.client = client; }

    public MeResponse get(String personId) {
        return client.get("/v1/api/me/" + personId, MeResponse.class);
    }
}
