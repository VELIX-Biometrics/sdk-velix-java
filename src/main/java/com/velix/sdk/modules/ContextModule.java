package com.velix.sdk.modules;

import com.velix.sdk.VelixClient;

import java.util.List;
import java.util.Map;

/**
 * /v1/contexts/* — Identity Context (Velix.ID). BearerAuth (session JWT).
 * See code/lib/lib-velix-contracts/openapi/public-api.yaml, tag "Identity Context".
 */
@SuppressWarnings("unchecked")
public final class ContextModule {

    private final VelixClient client;

    public ContextModule(VelixClient client) { this.client = client; }

    /** POST /v1/contexts */
    public Map<String, Object> create(Map<String, Object> payload) {
        return client.post("/v1/contexts", payload, Map.class);
    }

    /** GET /v1/contexts/{id} */
    public Map<String, Object> get(String id) {
        return client.get("/v1/contexts/" + id, Map.class);
    }

    /** GET /v1/contexts */
    public Map<String, Object> list() {
        return client.get("/v1/contexts", Map.class);
    }

    /** PATCH /v1/contexts/{id} */
    public Map<String, Object> update(String id, Map<String, Object> payload) {
        return client.patch("/v1/contexts/" + id, payload, Map.class);
    }

    /** DELETE /v1/contexts/{id} — soft delete */
    public void remove(String id) {
        client.delete("/v1/contexts/" + id);
    }

    /** POST /v1/contexts/{contextId}/authorize — Authorization Engine */
    public Map<String, Object> authorize(String contextId, Map<String, Object> payload) {
        return client.post("/v1/contexts/" + contextId + "/authorize", payload, Map.class);
    }

    /** GET /v1/contexts/{contextId}/authorization-decisions */
    public Map<String, Object> listAuthorizationDecisions(String contextId) {
        return client.get("/v1/contexts/" + contextId + "/authorization-decisions", Map.class);
    }

    /**
     * POST /v1/contexts/{contextId}/link-requests — solicita vínculo cross-tenant.
     * Nunca cria membership diretamente: retorna 202 (PENDING) aguardando
     * consentimento via magic link/notificação. A API pública não expõe
     * approve/reject — isso acontece fora do SDK.
     */
    public Map<String, Object> createLinkRequest(String contextId, Map<String, Object> payload) {
        return client.post("/v1/contexts/" + contextId + "/link-requests", payload, Map.class);
    }

    public static final class Memberships {
        private final VelixClient client;

        public Memberships(VelixClient client) { this.client = client; }

        /** POST /v1/contexts/{contextId}/memberships */
        public Map<String, Object> create(String contextId, Map<String, Object> payload) {
            return client.post("/v1/contexts/" + contextId + "/memberships", payload, Map.class);
        }

        /** GET /v1/contexts/{contextId}/memberships */
        public Map<String, Object> listByContext(String contextId) {
            return client.get("/v1/contexts/" + contextId + "/memberships", Map.class);
        }

        /** GET /v1/identities/{identityId}/memberships */
        public Map<String, Object> listByIdentity(String identityId) {
            return client.get("/v1/identities/" + identityId + "/memberships", Map.class);
        }

        /** PATCH /v1/memberships/{membershipId}/status — status=revoked é saída de contexto (definitiva). */
        public Map<String, Object> updateStatus(String membershipId, String status) {
            return client.patch("/v1/memberships/" + membershipId + "/status", Map.of("status", status), Map.class);
        }

        /** POST /v1/memberships/{membershipId}/roles */
        public Map<String, Object> addRoles(String membershipId, List<String> roleIds) {
            return client.post("/v1/memberships/" + membershipId + "/roles", Map.of("roleIds", roleIds), Map.class);
        }

        /** POST /v1/memberships/{membershipId}/roles/remove */
        public Map<String, Object> removeRoles(String membershipId, List<String> roleIds) {
            return client.post("/v1/memberships/" + membershipId + "/roles/remove", Map.of("roleIds", roleIds), Map.class);
        }
    }

    public static final class Roles {
        private final VelixClient client;

        public Roles(VelixClient client) { this.client = client; }

        /** POST /v1/context-roles */
        public Map<String, Object> create(Map<String, Object> payload) {
            return client.post("/v1/context-roles", payload, Map.class);
        }

        /** GET /v1/context-roles?contextType=... */
        public Map<String, Object> list(String contextType) {
            return client.get("/v1/context-roles?contextType=" + contextType, Map.class);
        }

        /** POST /v1/context-roles/{roleId}/permissions */
        public Map<String, Object> linkPermissions(String roleId, List<String> permissionIds) {
            return client.post("/v1/context-roles/" + roleId + "/permissions", Map.of("permissionIds", permissionIds), Map.class);
        }
    }

    public static final class Permissions {
        private final VelixClient client;

        public Permissions(VelixClient client) { this.client = client; }

        /** POST /v1/context-permissions */
        public Map<String, Object> create(Map<String, Object> payload) {
            return client.post("/v1/context-permissions", payload, Map.class);
        }

        /** GET /v1/context-permissions?category=... */
        public Map<String, Object> list(String category) {
            String path = "/v1/context-permissions" + (category != null ? "?category=" + category : "");
            return client.get(path, Map.class);
        }
    }

    public static final class AuthorizationTokens {
        private final VelixClient client;

        public AuthorizationTokens(VelixClient client) { this.client = client; }

        /** POST /v1/authorization-tokens/validate */
        public Map<String, Object> validate(String token, boolean consume) {
            return client.post("/v1/authorization-tokens/validate", Map.of("token", token, "consume", consume), Map.class);
        }
    }
}
