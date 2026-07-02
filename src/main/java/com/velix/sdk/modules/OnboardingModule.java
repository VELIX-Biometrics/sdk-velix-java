package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;
import com.velix.sdk.models.OnboardingRequest;
import com.velix.sdk.models.OnboardingResponse;

/**
 * POST /v1/api/onboarding — scope onboarding:write (Velix.ID).
 */
public final class OnboardingModule {

    private final VelixClient client;

    public OnboardingModule(VelixClient client) { this.client = client; }

    public OnboardingResponse enroll(OnboardingRequest request) {
        return client.post("/v1/api/onboarding", request, OnboardingResponse.class);
    }
}
