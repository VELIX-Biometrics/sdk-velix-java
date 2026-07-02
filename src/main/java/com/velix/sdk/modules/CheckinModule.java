package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.CheckinIdentifyRequest;
import com.velix.sdk.models.CheckinIdentifyResponse;

/**
 * POST /v1/api/checkin/identify — scope checkin:write (Velix.ID).
 *
 * Único endpoint real de checkin exposto pela superfície de API key. Chama
 * exatamente CheckinService.identifyFace() no identity-core — o mesmo método
 * usado pelo fluxo público HMAC (/v1/public/checkin/{tenantSlug}/identify).
 *
 * Os antigos métodos facial(tenantSlug, frame) / qr(...) / pin(...) apontavam
 * para /v1/checkin/{tenantSlug}/identify, endpoint que não existe na
 * superfície de API key — removidos nesta revisão (task #656 / spec #593).
 */
public final class CheckinModule {

    private final VelixClient client;

    public CheckinModule(VelixClient client) { this.client = client; }

    public CheckinIdentifyResponse identify(CheckinIdentifyRequest request) {
        return client.post("/v1/api/checkin/identify", request, CheckinIdentifyResponse.class);
    }
}
