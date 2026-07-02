package com.velix.sdk;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TenantsModuleTest {

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
    void me_returnsTenant() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"tenant-uuid","name":"Acme Corp","slug":"acme","plan":"enterprise","maxPersons":1000}}
                """));

        var tenant = client.tenants().me();

        assertEquals("tenant-uuid", tenant.id());
        assertEquals("acme", tenant.slug());
        assertEquals("enterprise", tenant.plan());
    }

    @Test
    void updateSettings_returnsUpdatedTenant() {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody("""
                {"data":{"id":"tenant-uuid","requireLiveness":true,"timezone":"America/Sao_Paulo"}}
                """));

        var settings = new com.velix.sdk.models.TenantSettings(true, "America/Sao_Paulo");
        var tenant = client.tenants().updateSettings(settings);

        assertTrue(tenant.requireLiveness());
        assertEquals("America/Sao_Paulo", tenant.timezone());
    }
}
