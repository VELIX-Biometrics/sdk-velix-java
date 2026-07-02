package com.velix.sdk;

import com.velix.sdk.models.OnboardingRequest;
import com.velix.sdk.models.OnboardingResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OnboardingModuleTest {

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
    void enroll_postsToApiOnboarding_andUnwrapsEnvelope() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"person_id":"p-1","identity_id":"i-1","enrolled":true,
                "frames_processed":3,"frames_results":[],"embedding_id":"e-1",
                "message":"ok"}}
                """));

        OnboardingRequest req = OnboardingRequest.builder()
            .name("Maria Silva")
            .frames(List.of("base64frame1", "base64frame2", "base64frame3"))
            .build();

        OnboardingResponse res = client.onboarding().enroll(req);

        assertEquals("p-1", res.personId());
        assertTrue(res.enrolled());
        assertEquals(3, res.framesProcessed());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/onboarding", recorded.getPath());
        assertEquals("vlx_test_key", recorded.getHeader("x-api-key"));
        assertTrue(recorded.getBody().readUtf8().contains("\"frames\""));
    }

    @Test
    void builder_requiresNameAndFrames() {
        assertThrows(IllegalArgumentException.class, () ->
            OnboardingRequest.builder().name("").frames(List.of("a")).build());
        assertThrows(IllegalArgumentException.class, () ->
            OnboardingRequest.builder().name("x").frames(List.of()).build());
    }
}
