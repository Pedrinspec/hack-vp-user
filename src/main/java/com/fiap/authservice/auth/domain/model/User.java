package com.fiap.authservice.auth.domain.model;

import com.fiap.authservice.auth.domain.valueobject.Email;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Email email;
    private final String passwordHash;
    private final Instant createdAt;

    private User(UUID id, Email email, String passwordHash, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "Id is required");
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt is required");
    }

    public static User create(Email email, String passwordHash) {
        return new User(UUID.randomUUID(), email, passwordHash, Instant.now());
    }

    public static User rehydrate(UUID id, Email email, String passwordHash, Instant createdAt) {
        return new User(id, email, passwordHash, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
