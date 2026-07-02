package com.velix.sdk.modules;

import com.fasterxml.jackson.databind.JsonNode;
import com.velix.sdk.VelixClient;
import com.velix.sdk.models.VelixEvent;

import java.util.List;
import java.util.Map;

public final class EventsModule {

    private final VelixClient client;

    public EventsModule(VelixClient client) { this.client = client; }

    public List<VelixEvent> list(int page, int limit) {
        JsonNode res = client.get("/v1/events?page=" + page + "&limit=" + limit, JsonNode.class);
        return client.mapper.convertValue(res.get("items"),
            client.mapper.getTypeFactory().constructCollectionType(List.class, VelixEvent.class));
    }

    public VelixEvent get(String id) {
        return client.get("/v1/events/" + id, VelixEvent.class);
    }

    public VelixEvent create(Map<String, Object> data) {
        return client.post("/v1/events", data, VelixEvent.class);
    }

    public VelixEvent configure(String id, Map<String, Object> config) {
        return client.patch("/v1/events/" + id + "/config", config, VelixEvent.class);
    }
}
