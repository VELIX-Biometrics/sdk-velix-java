# com.velix:velix-sdk — Java SDK ![version](https://img.shields.io/badge/version-0.1.0--alpha.1-blue)

> ⚠️ **Alpha / pre-release.** This SDK targets a public API surface that does not yet fully exist on the VELIX backend (see internal task #593). Endpoints and auth may not work against production. Do not use in production integrations yet.

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
    <version>1.0.0</version>
</dependency>
```

**Gradle:**
```kotlin
implementation("com.velix:velix-sdk:1.0.0")
```

## Quick Start

```java
import com.velix.sdk.VelixClient;

var client = VelixClient.builder()
    .apiUrl(System.getenv("VELIX_API_URL"))
    .apiKey(System.getenv("VELIX_API_KEY"))
    .build();

var result = client.checkin().facial("tenant-slug", frameBase64);
System.out.println(result.passed()); // true | false
```

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `VELIX_API_URL` | Yes | API base URL (`https://api.velixbiometrics.com`) |
| `VELIX_API_KEY` | Yes | Tenant API key (`vx_live_...` or `vx_sandbox_...`) |

## Modules

| Module | Methods |
|--------|---------|
| `client.checkin()` | `facial()`, `qr()`, `pin()`, `getHistory()` |
| `client.persons()` | `list()`, `get()`, `create()`, `update()`, `delete()`, `enroll()` |
| `client.events()` | `list()`, `get()`, `create()`, `configure()` |
| `client.tenants()` | `me()`, `updateSettings()` |

## Checkin Module

```java
var checkin = client.checkin();

// Facial identification (base64 JPEG frame)
var result = checkin.facial("tenant-slug", frameBase64);
// result.passed() == true
// result.personId() == "uuid"
// result.personName() == "João Silva"

// QR code checkin
var result = checkin.qr("tenant-slug", qrToken);

// PIN checkin
var result = checkin.pin("tenant-slug", pin);

// Paginated history
var history = checkin.getHistory("tenant-slug", 1, 20);
```

## Persons Module

```java
var persons = client.persons();

// List
var list = persons.list(1, 20);

// Get by ID
var person = persons.get("uuid");

// Create
var created = persons.create(Map.of(
    "name",       "João Silva",
    "email",      "joao@company.com",
    "externalId", "EMP-001"
));

// Update
persons.update("uuid", Map.of("name", "João B. Silva"));

// Enroll biometrics (minimum 3 base64 frames)
persons.enroll("uuid", List.of(frame1, frame2, frame3));

// Delete
persons.delete("uuid");
```

## Events Module

```java
var events = client.events();

var list    = events.list(1, 20);
var event   = events.get("uuid");
var created = events.create(Map.of("name", "Annual Conference 2026"));
events.configure("uuid", Map.of("checkInOpen", true));
```

## Tenants Module

```java
var tenant = client.tenants().me();
client.tenants().updateSettings(Map.of("requireLiveness", true));
```

## Error Handling

```java
import com.velix.sdk.exceptions.AuthException;
import com.velix.sdk.exceptions.BiometricException;
import com.velix.sdk.exceptions.RateLimitException;
import com.velix.sdk.exceptions.VelixException;

try {
    var result = client.checkin().facial("slug", frame);
} catch (AuthException e) {
    System.err.println("Invalid API key");
} catch (BiometricException e) {
    System.err.println("Face not recognized or liveness failed");
} catch (RateLimitException e) {
    System.err.printf("Rate limit — retry after %dms%n", e.getRetryAfter());
} catch (VelixException e) {
    System.err.printf("HTTP %d: %s%n", e.getStatusCode(), e.getMessage());
}
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
