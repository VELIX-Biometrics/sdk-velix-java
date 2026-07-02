package com.velix.sdk;

import com.velix.sdk.exceptions.AuthException;
import com.velix.sdk.models.Person;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonsModuleTest {

    MockWebServer server;
    VelixClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        client = VelixClient.builder()
            .apiUrl(server.url("/").toString().replaceAll("/$", ""))
            .apiKey("vx_test_key")
            .build();
    }

    @AfterEach
    void tearDown() throws IOException { server.shutdown(); }

    @Test
    void list_returnsPeople() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"items":[
                  {"id":"p-1","name":"Maria","email":"maria@test.com","has_biometric":true,"status":"active","created_at":"2026-01-01"}
                ],"total":1}}
                """));

        List<Person> people = client.persons().list(1, 20);

        assertEquals(1, people.size());
        assertEquals("Maria", people.get(0).name());
        assertTrue(people.get(0).hasBiometric());
    }

    @Test
    void create_returnsPerson() {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"p-2","name":"Carlos","email":"carlos@test.com","has_biometric":false,"status":"active","created_at":"2026-01-02"}}
                """));

        Person p = client.persons().create(Map.of("name", "Carlos", "email", "carlos@test.com"));

        assertEquals("p-2", p.id());
        assertEquals("Carlos", p.name());
    }

    @Test
    void get_throws401_whenUnauthorized() {
        server.enqueue(new MockResponse().setResponseCode(401).setBody("Unauthorized"));

        assertThrows(AuthException.class, () -> client.persons().get("p-999"));
    }
}
