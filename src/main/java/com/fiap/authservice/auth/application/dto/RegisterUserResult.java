package com.fiap.authservice.auth.application.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterUserResult(UUID userId, String email, Instant createdAt) {
}
