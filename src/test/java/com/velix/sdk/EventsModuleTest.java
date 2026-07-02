package com.velix.sdk;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EventsModuleTest {

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
    void list_returnsPagedEvents() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"items":[{"id":"evt-1","name":"Tech Summit","status":"active"}],"total":1,"page":1,"limit":20}}
                """));

        var result = client.events().list(1, 20);

        assertEquals(1, result.total());
        assertEquals(1, result.items().size());
        assertEquals("evt-1", result.items().get(0).id());
    }

    @Test
    void get_returnsEvent() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"evt-1","name":"Tech Summit","status":"active"}}
                """));

        var event = client.events().get("evt-1");

        assertEquals("evt-1", event.id());
        assertEquals("Tech Summit", event.name());
    }

    @Test
    void get_throwsOnNotFound() {
        server.enqueue(new MockResponse()
            .setResponseCode(404)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"message":"Event not found","code":"NOT_FOUND"}
                """));

        assertThrows(com.velix.sdk.exceptions.VelixException.class,
            () -> client.events().get("nonexistent"));
    }

    @Test
    void create_returnsNewEvent() {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"evt-new","name":"New Event","status":"draft"}}
                """));

        var event = client.events().create("New Event");

        assertEquals("evt-new", event.id());
        assertEquals("draft", event.status());
    }
}
