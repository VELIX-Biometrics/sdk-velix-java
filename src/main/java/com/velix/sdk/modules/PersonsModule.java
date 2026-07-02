package com.velix.sdk.modules;

import com.fasterxml.jackson.databind.JsonNode;
import com.velix.sdk.VelixClient;
import com.velix.sdk.models.Person;

import java.util.List;
import java.util.Map;

public final class PersonsModule {

    private final VelixClient client;

    public PersonsModule(VelixClient client) { this.client = client; }

    public List<Person> list(int page, int limit) {
        JsonNode res = client.get("/v1/persons?page=" + page + "&limit=" + limit, JsonNode.class);
        return client.mapper.convertValue(res.get("items"),
            client.mapper.getTypeFactory().constructCollectionType(List.class, Person.class));
    }

    public Person get(String id) {
        return client.get("/v1/persons/" + id, Person.class);
    }

    public Person create(Map<String, Object> data) {
        return client.post("/v1/persons", data, Person.class);
    }

    public Person update(String id, Map<String, Object> data) {
        return client.put("/v1/persons/" + id, data, Person.class);
    }

    public void delete(String id) {
        client.delete("/v1/persons/" + id);
    }

    public void enroll(String id, List<String> frames) {
        client.post("/v1/persons/" + id + "/enroll", Map.of("frames", frames), Void.class);
    }
}
