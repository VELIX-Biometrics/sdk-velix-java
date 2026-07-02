package com.velix.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velix.sdk.exceptions.AuthException;
import com.velix.sdk.exceptions.RateLimitException;
import com.velix.sdk.exceptions.VelixException;
import com.velix.sdk.modules.CheckinModule;
import com.velix.sdk.modules.EventsModule;
import com.velix.sdk.modules.PersonsModule;
import com.velix.sdk.modules.TenantsModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class VelixClient {

    static final String USER_AGENT = "velix-java-sdk/1.0.0";

    final VelixConfig config;
    final HttpClient http;
    final ObjectMapper mapper;

    private final CheckinModule checkin;
    private final PersonsModule persons;
    private final EventsModule events;
    private final TenantsModule tenants;

    private VelixClient(VelixConfig config) {
        this.config = config;
        this.http = HttpClient.newBuilder()
            .connectTimeout(config.timeout())
            .build();
        this.mapper = new ObjectMapper();
        this.checkin = new CheckinModule(this);
        this.persons = new PersonsModule(this);
        this.events = new EventsModule(this);
        this.tenants = new TenantsModule(this);
    }

    public static Builder builder() { return new Builder(); }

    public CheckinModule checkin()   { return checkin; }
    public PersonsModule persons()   { return persons; }
    public EventsModule events()     { return events; }
    public TenantsModule tenants()   { return tenants; }

    public <T> T get(String path, Class<T> type) {
        return request("GET", path, null, type);
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return request("POST", path, body, type);
    }

    public <T> T put(String path, Object body, Class<T> type) {
        return request("PUT", path, body, type);
    }

    public <T> T patch(String path, Object body, Class<T> type) {
        return request("PATCH", path, body, type);
    }

    public void delete(String path) {
        request("DELETE", path, null, Void.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T request(String method, String path, Object body, Class<T> type) {
        int attempt = 0;
        while (true) {
            try {
                String bodyStr = body != null ? mapper.writeValueAsString(body) : "";
                HttpRequest.Builder req = HttpRequest.newBuilder()
                    .uri(URI.create(config.apiUrl() + path))
                    .header("x-api-key", config.apiKey())
                    .header("Content-Type", "application/json")
                    .header("User-Agent", USER_AGENT)
                    .timeout(config.timeout());

                req = switch (method) {
                    case "GET"    -> req.GET();
                    case "DELETE" -> req.DELETE();
                    case "POST"   -> req.POST(HttpRequest.BodyPublishers.ofString(bodyStr));
                    case "PUT"    -> req.PUT(HttpRequest.BodyPublishers.ofString(bodyStr));
                    case "PATCH"  -> req.method("PATCH", HttpRequest.BodyPublishers.ofString(bodyStr));
                    default       -> throw new IllegalArgumentException("Unknown method: " + method);
                };

                HttpResponse<String> res = http.send(req.build(), HttpResponse.BodyHandlers.ofString());
                int status = res.statusCode();

                if (status == 401) throw new AuthException("Unauthorized — check your API key");
                if (status == 429) {
                    long retryAfter = res.headers().firstValueAsLong("Retry-After").orElse(1L);
                    if (attempt < config.maxRetries()) {
                        Thread.sleep(retryAfter * 1000L * (long) Math.pow(2, attempt));
                        attempt++;
                        continue;
                    }
                    throw new RateLimitException(retryAfter);
                }
                if (status == 503 && attempt < config.maxRetries()) {
                    Thread.sleep(1000L * (long) Math.pow(2, attempt));
                    attempt++;
                    continue;
                }
                if (status >= 400) throw new VelixException(res.body(), status);

                if (type == Void.class) return null;

                // unwrap envelope { data: T }
                var node = mapper.readTree(res.body());
                var data = node.has("data") ? node.get("data") : node;
                return mapper.treeToValue(data, type);

            } catch (VelixException e) {
                throw e;
            } catch (IOException | InterruptedException e) {
                if (attempt < config.maxRetries()) { attempt++; continue; }
                throw new VelixException("Request failed: " + e.getMessage(), 0);
            }
        }
    }

    public static final class Builder {
        private String apiUrl;
        private String apiKey;
        private Duration timeout = Duration.ofSeconds(30);
        private int maxRetries = 3;

        public Builder apiUrl(String v)    { this.apiUrl = v; return this; }
        public Builder apiKey(String v)    { this.apiKey = v; return this; }
        public Builder timeout(Duration v) { this.timeout = v; return this; }
        public Builder maxRetries(int v)   { this.maxRetries = v; return this; }

        public VelixClient build() {
            return new VelixClient(new VelixConfig(apiUrl, apiKey, timeout, maxRetries));
        }
    }
}
