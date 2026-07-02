package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * EventGuest — inclui status de checkin do convidado. Retornado tanto por
 * POST /v1/api/events/{id}/guests quanto por GET .../guests/{guestId}.
 * Campos já definidos em camelCase na spec (eventId, categoryId).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GuestResponse(
    String id,
    String eventId,
    String name,
    String email,
    String status,
    String categoryId
) {}
