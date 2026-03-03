package com.fiap.authservice.auth.domain.entity;

import java.time.Instant;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        String passwordHash,
        Instant createdAt,
        Instant updatedAt
) {
}
