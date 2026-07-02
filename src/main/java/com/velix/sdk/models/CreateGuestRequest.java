package com.velix.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Contrato real de POST /v1/api/events/{id}/guests (scope events:write).
 * Espelha src/modules/events/dto/create-guest.dto.ts. Campos já definidos
 * em camelCase na spec (birthDate, categoryId, companionOf).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateGuestRequest(
    String name,
    String email,
    String cpf,
    String phone,
    String birthDate,
    String categoryId,
    String companionOf
) {
    public CreateGuestRequest {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email required");
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String name;
        private String email;
        private String cpf;
        private String phone;
        private String birthDate;
        private String categoryId;
        private String companionOf;

        public Builder name(String v) { this.name = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder cpf(String v) { this.cpf = v; return this; }
        public Builder phone(String v) { this.phone = v; return this; }
        public Builder birthDate(String v) { this.birthDate = v; return this; }
        public Builder categoryId(String v) { this.categoryId = v; return this; }
        public Builder companionOf(String v) { this.companionOf = v; return this; }

        public CreateGuestRequest build() {
            return new CreateGuestRequest(name, email, cpf, phone, birthDate, categoryId, companionOf);
        }
    }
}
