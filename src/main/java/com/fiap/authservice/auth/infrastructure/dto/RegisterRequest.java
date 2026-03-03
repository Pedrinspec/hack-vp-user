package com.fiap.authservice.auth.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "username é obrigatório")
        @Size(min = 3, max = 50, message = "username deve ter entre 3 e 50 caracteres")
        String username,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "password é obrigatório")
        @Size(min = 8, max = 64, message = "password deve ter entre 8 e 64 caracteres")
        String password
) {
}
