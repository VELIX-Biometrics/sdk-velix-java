package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.CreateGuestRequest;
import com.velix.sdk.models.GuestResponse;

/**
 * Velix Events — cobertura mínima na superfície de API key: criar e
 * consultar convidado de evento.
 *
 * Os antigos métodos list()/get()/create()/configure() apontavam para
 * /v1/events/*, endpoints do dashboard admin (JWT) que não existem na
 * superfície de API key — removidos nesta revisão (task #656 / spec #593).
 */
public final class EventsModule {

    private final VelixClient client;

    public EventsModule(VelixClient client) { this.client = client; }

    /** POST /v1/api/events/{id}/guests — scope events:write. */
    public GuestResponse createGuest(String eventId, CreateGuestRequest request) {
        return client.post("/v1/api/events/" + eventId + "/guests", request, GuestResponse.class);
    }

    /** GET /v1/api/events/{id}/guests/{guestId} — scope events:read. */
    public GuestResponse getGuest(String eventId, String guestId) {
        return client.get("/v1/api/events/" + eventId + "/guests/" + guestId, GuestResponse.class);
    }
}
