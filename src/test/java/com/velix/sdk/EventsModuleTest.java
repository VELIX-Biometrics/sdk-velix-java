package com.velix.sdk;

import com.velix.sdk.models.CreateGuestRequest;
import com.velix.sdk.models.GuestResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
            .apiKey("vlx_test_key")
            .build();
    }

    @AfterEach
    void tearDown() throws IOException { server.shutdown(); }

    @Test
    void createGuest_postsToApiEventsGuests() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(201)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"guest-1","eventId":"evt-1","name":"João","email":"joao@test.com","status":"invited","categoryId":null}}
                """));

        CreateGuestRequest req = CreateGuestRequest.builder()
            .name("João")
            .email("joao@test.com")
            .build();

        GuestResponse guest = client.events().createGuest("evt-1", req);

        assertEquals("guest-1", guest.id());
        assertEquals("evt-1", guest.eventId());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/events/evt-1/guests", recorded.getPath());
    }

    @Test
    void getGuest_getsFromApiEventsGuests() throws InterruptedException {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"guest-1","eventId":"evt-1","name":"João","email":"joao@test.com","status":"checked_in","categoryId":null}}
                """));

        GuestResponse guest = client.events().getGuest("evt-1", "guest-1");

        assertEquals("checked_in", guest.status());

        RecordedRequest recorded = server.takeRequest();
        assertEquals("/v1/api/events/evt-1/guests/guest-1", recorded.getPath());
    }

    @Test
    void createGuest_requiresNameAndEmail() {
        assertThrows(IllegalArgumentException.class, () ->
            CreateGuestRequest.builder().email("a@b.com").build());
        assertThrows(IllegalArgumentException.class, () ->
            CreateGuestRequest.builder().name("a").build());
    }
}
