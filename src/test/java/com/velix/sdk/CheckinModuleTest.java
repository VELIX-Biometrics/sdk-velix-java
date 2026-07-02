package com.velix.sdk;

import com.velix.sdk.models.CheckinResult;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CheckinModuleTest {

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
    void facial_returnsPassedTrue() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"passed":true,"person_id":"uuid-123","name":"João Silva","access_log_id":"log-1"}}
                """));

        CheckinResult result = client.checkin().facial("tenant-slug", "base64frame==");

        assertTrue(result.passed());
        assertEquals("uuid-123", result.personId());
        assertEquals("João Silva", result.name());
    }

    @Test
    void facial_returnsPassedFalse_whenNotIdentified() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"passed":false,"person_id":null,"name":null,"access_log_id":"log-2"}}
                """));

        CheckinResult result = client.checkin().facial("tenant-slug", "base64frame==");

        assertFalse(result.passed());
        assertNull(result.personId());
    }
}
