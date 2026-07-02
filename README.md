# com.velix:velix-sdk — Java SDK ![version](https://img.shields.io/badge/version-0.1.0--alpha.1-blue)

> ⚠️ **Alpha / pre-release.** This SDK targets the real `/v1/api/*` API-key surface of
> `api-velix-identity-core`, defined by the reference contract at
> `lib-velix-contracts/openapi/public-api.yaml` (task #593). It covers exactly the 6
> endpoints that exist today. Velix Time has no API-key surface yet — do not expect
> Time methods here. Do not use in production integrations yet.

Official Java SDK for the VELIX Biometrics platform — facial access control B2B SaaS.

## Requirements

- Java 17+
- Maven 3.9+ or Gradle 8+

## Installation

**Maven:**
```xml
<dependency>
    <groupId>com.velix</groupId>
    <artifactId>velix-sdk</artifactId>
    <version>0.1.0-alpha.1</version>
</dependency>
```

**Gradle:**
```kotlin
implementation("com.velix:velix-sdk:0.1.0-alpha.1")
```

## Quick Start

```java
import com.velix.sdk.VelixClient;
import com.velix.sdk.models.CheckinIdentifyRequest;

var client = VelixClient.builder()
    .apiUrl(System.getenv("VELIX_API_URL"))
    .apiKey(System.getenv("VELIX_API_KEY")) // vlx_...
    .build();

var result = client.checkin().identify(
    CheckinIdentifyRequest.builder().imageBase64(frameBase64).build()
);
System.out.println(result.matched()); // true | false
```

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `VELIX_API_URL` | Yes | API base URL (`https://api.velixbiometrics.com`) |
| `VELIX_API_KEY` | Yes | API key (`vlx_...`), sent as `x-api-key` header |

## Auth

Every request is authenticated with the `x-api-key: vlx_...` header (set automatically
from `VelixConfig.apiKey()`). The server also accepts `Authorization: Bearer vlx_...`
as an alternative, but this SDK only ever sends `x-api-key` — never mix it with JWT
Bearer auth, which is a different guard entirely.

Each API key carries `scopes`. Calling an endpoint without the required scope returns
an HTTP 4xx handled as `VelixException`.

| Module method | Required scope |
|----------------|-----------------|
| `client.onboarding().enroll(...)` | `onboarding:write` |
| `client.checkin().identify(...)` | `checkin:write` |
| `client.lgpd().requestDeletion(...)` | `lgpd:write` |
| `client.me().get(...)` | `me:read` |
| `client.events().createGuest(...)` | `events:write` |
| `client.events().getGuest(...)` | `events:read` |

## Modules

| Module | Endpoint | Methods |
|--------|----------|---------|
| `client.onboarding()` | `POST /v1/api/onboarding` | `enroll(OnboardingRequest)` |
| `client.checkin()` | `POST /v1/api/checkin/identify` | `identify(CheckinIdentifyRequest)` |
| `client.lgpd()` | `POST /v1/api/deletion-request` | `requestDeletion(String personId)` |
| `client.me()` | `GET /v1/api/me/{personId}` | `get(String personId)` |
| `client.events()` | `POST /v1/api/events/{id}/guests`, `GET /v1/api/events/{id}/guests/{guestId}` | `createGuest(...)`, `getGuest(...)` |

These are the **only 6 endpoints** that exist on the API-key surface today. Any
previous SDK method not listed above (`persons()`, `tenants()`, `checkin().facial/qr/pin()`,
`events().list/get/create/configure()`) pointed at endpoints that never existed on this
surface and were removed in the realignment for task #656.

## Onboarding Module

```java
import com.velix.sdk.models.OnboardingRequest;

var request = OnboardingRequest.builder()
    .name("João Silva")
    .frames(List.of(frame1Base64, frame2Base64, frame3Base64)) // min 1, tenant enroll_frames default 3
    .email("joao@company.com")
    .externalId("EMP-001")
    .build();

var result = client.onboarding().enroll(request);
// result.personId(), result.identityId(), result.enrolled(), result.framesProcessed()
```

## Checkin Module

```java
import com.velix.sdk.models.CheckinIdentifyRequest;

var result = client.checkin().identify(
    CheckinIdentifyRequest.builder()
        .imageBase64(frameBase64)
        .topK(3)
        .build()
);
// result.matched(), result.personId(), result.qualityScore()
// Liveness score is NEVER returned by the API — only pass/fail is embedded in matched/message.
```

## LGPD Module

```java
var deletion = client.lgpd().requestDeletion(personId);
// deletion.protocolNumber()
```

## Me Module

```java
var me = client.me().get(personId);
// me.id(), me.name(), me.email(), me.photoUrl()
```

## Events Module (Velix Events — minimal coverage)

```java
import com.velix.sdk.models.CreateGuestRequest;

var guest = client.events().createGuest(eventId,
    CreateGuestRequest.builder().name("João").email("joao@test.com").build());

var fetched = client.events().getGuest(eventId, guest.id());
// fetched.status() — includes checkin status
```

## Velix Time

Velix Time has **no** endpoint on the `/v1/api/*` API-key surface today — the
`GatewayModule` of `api-velix-identity-core` proxies edge, intelligence, copilot and
marketplace, but not `api-velix-time`. This SDK does not implement any Time-related
method. If a future revision adds one prematurely, it must throw
`UnsupportedOperationException` and be documented here as a stub — never appear
functional.

## Error Handling

```java
import com.velix.sdk.exceptions.AuthException;
import com.velix.sdk.exceptions.RateLimitException;
import com.velix.sdk.exceptions.VelixException;

try {
    var result = client.checkin().identify(request);
} catch (AuthException e) {
    System.err.println("Invalid API key");
} catch (RateLimitException e) {
    System.err.printf("Rate limit — retry after %ds%n", e.getRetryAfterSeconds());
} catch (VelixException e) {
    System.err.printf("HTTP %d: %s%n", e.getStatusCode(), e.getMessage());
}
```

## Timeout & Retries

Default HTTP client timeout is **30000ms (30s)**, per the SDK contract notes in
`public-api.yaml` (biometric payloads with liveness samples can be 6-12MB). Override it
via the builder:

```java
var client = VelixClient.builder()
    .apiUrl(apiUrl)
    .apiKey(apiKey)
    .timeout(Duration.ofSeconds(45))
    .maxRetries(5)
    .build();
```

## Running Tests

```bash
mvn test
mvn test -Dtest=CheckinModuleTest    # single test class
```

## Local Development

```bash
mvn compile
mvn test
mvn package -DskipTests
mvn install -DskipTests    # install to local Maven repo
```

## Get an API Key

Access the dashboard at **velixbiometrics.com** → Settings → API Keys → New Key.
