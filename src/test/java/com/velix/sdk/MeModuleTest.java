package com.velix.sdk;

import com.velix.sdk.exceptions.AuthException;
import com.velix.sdk.models.MeResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MeModuleTest {

    MockWebServer server;
    VelixClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        client = VelixClient.builder()
            .apiUrl(server.url("/").toString().replaceAll("/$", ""))
            .apiKey("vlx_test_key")
            .build();
    }

    @AfterEach
    void tearDown() throws IOException { server.shutdown(); }

    @Test
    void get_getsFromApiMePersonId() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"p-1","name":"Maria","email":"maria@test.com","phone":null,
                "photo_url":null,"created_at":"2026-01-01T00:00:00Z"}}
                """));

        MeResponse me = client.me().get("p-1");

        assertEquals("p-1", me.id());
        assertEquals("Maria", me.name());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/me/p-1", recorded.getPath());
    }

    @Test
    void get_throws401_whenUnauthorized() {
        server.enqueue(new MockResponse().setResponseCode(401).setBody("Unauthorized"));

        assertThrows(AuthException.class, () -> client.me().get("p-999"));
    }
}
