package com.velix.sdk.modules;

import com.fasterxml.jackson.databind.JsonNode;
import com.velix.sdk.VelixClient;

import java.util.Map;

public final class TenantsModule {

    private final VelixClient client;

    public TenantsModule(VelixClient client) { this.client = client; }

    public JsonNode me() {
        return client.get("/v1/tenants/me", JsonNode.class);
    }

    public JsonNode updateSettings(Map<String, Object> settings) {
        return client.put("/v1/tenants/me/settings", settings, JsonNode.class);
    }
}
