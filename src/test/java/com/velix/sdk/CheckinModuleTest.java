package com.velix.sdk;

import com.velix.sdk.models.CheckinIdentifyRequest;
import com.velix.sdk.models.CheckinIdentifyResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
            .apiKey("vlx_test_key")
            .build();
    }

    @AfterEach
    void tearDown() throws IOException { server.shutdown(); }

    @Test
    void identify_postsToApiCheckinIdentify_andUnwrapsEnvelope() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"matched":true,"person_id":"uuid-123","quality_score":0.92,"message":"ok"}}
                """));

        CheckinIdentifyRequest req = CheckinIdentifyRequest.builder()
            .imageBase64("base64frame==")
            .build();

        CheckinIdentifyResponse result = client.checkin().identify(req);

        assertTrue(result.matched());
        assertEquals("uuid-123", result.personId());
        assertEquals(0.92, result.qualityScore());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/checkin/identify", recorded.getPath());
        assertEquals("vlx_test_key", recorded.getHeader("x-api-key"));
    }

    @Test
    void identify_returnsMatchedFalse_whenNotIdentified() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"matched":false,"person_id":null,"quality_score":0.1,"message":"no match"}}
                """));

        CheckinIdentifyResponse result = client.checkin().identify(
            CheckinIdentifyRequest.builder().imageBase64("base64frame==").build());

        assertFalse(result.matched());
        assertNull(result.personId());
    }

    @Test
    void imageBase64_isRequired() {
        assertThrows(IllegalArgumentException.class, () ->
            CheckinIdentifyRequest.builder().build());
    }
}
