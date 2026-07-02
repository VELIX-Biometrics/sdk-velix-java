package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.CheckinResult;

import java.util.Map;

public final class CheckinModule {

    private final VelixClient client;

    public CheckinModule(VelixClient client) { this.client = client; }

    public CheckinResult facial(String tenantSlug, String frameBase64) {
        return client.post(
            "/v1/checkin/" + tenantSlug + "/identify",
            Map.of("frame", frameBase64, "type", "facial"),
            CheckinResult.class
        );
    }

    public CheckinResult qr(String tenantSlug, String qrCode) {
        return client.post(
            "/v1/checkin/" + tenantSlug + "/identify",
            Map.of("code", qrCode, "type", "qr"),
            CheckinResult.class
        );
    }

    public CheckinResult pin(String tenantSlug, String pin) {
        return client.post(
            "/v1/checkin/" + tenantSlug + "/identify",
            Map.of("pin", pin, "type", "pin"),
            CheckinResult.class
        );
    }
}
