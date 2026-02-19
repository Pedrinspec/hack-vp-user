package com.example.authservice.auth.domain.entity;

import com.example.authservice.auth.domain.vo.Email;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID id;
    private final Email email;
    private final String passwordHash;
    private final Instant createdAt;

    private User(UUID id, Email email, String passwordHash, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "password hash cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }

    public static User create(Email email, String passwordHash) {
        if (passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be blank");
        }
        return new User(UUID.randomUUID(), email, passwordHash, Instant.now());
    }

    public static User restore(UUID id, Email email, String passwordHash, Instant createdAt) {
        return new User(id, email, passwordHash, createdAt);
    }

    public UUID id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
