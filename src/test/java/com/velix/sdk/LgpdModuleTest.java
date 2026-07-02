package com.velix.sdk;

import com.velix.sdk.models.DeletionRequestResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LgpdModuleTest {

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
    void requestDeletion_postsToApiDeletionRequest() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"protocol_number":"PROT-123","message":"Solicitação registrada"}}
                """));

        DeletionRequestResponse res = client.lgpd().requestDeletion("person-uuid");

        assertEquals("PROT-123", res.protocolNumber());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/deletion-request", recorded.getPath());
        assertTrue(recorded.getBody().readUtf8().contains("person-uuid"));
    }
}
