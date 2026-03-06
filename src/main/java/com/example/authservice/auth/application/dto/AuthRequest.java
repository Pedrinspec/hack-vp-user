package com.example.authservice.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        String name,
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
