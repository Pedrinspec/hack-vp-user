package com.fiap.authservice.auth.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email ou username é obrigatório")
        String identifier,

        @NotBlank(message = "password é obrigatório")
        String password
) {
}
