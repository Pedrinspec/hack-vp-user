package com.fiap.authservice.auth.domain;

import com.fiap.authservice.auth.domain.entity.User;
import com.fiap.authservice.auth.domain.vo.Email;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void shouldCreateUserWithGeneratedIdAndCreatedAt() {
        Email email = Email.of("user@example.com");

        User user = User.create(email, "hashed-password");

        assertNotNull(user.id());
        assertNotNull(user.createdAt());
        assertEquals(email, user.email());
        assertEquals("hashed-password", user.passwordHash());
    }

    @Test
    void shouldRejectBlankPasswordHashOnCreate() {
        Email email = Email.of("user@example.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.create(email, "   ")
        );

        assertEquals("Password hash cannot be blank", exception.getMessage());
    }

    @Test
    void shouldRestorePersistedUserState() {
        UUID id = UUID.randomUUID();
        Email email = Email.of("restored@example.com");
        Instant createdAt = Instant.parse("2026-01-01T10:15:30Z");

        User user = User.restore(id, email, "persisted-hash", createdAt);

        assertEquals(id, user.id());
        assertEquals(email, user.email());
        assertEquals("persisted-hash", user.passwordHash());
        assertEquals(createdAt, user.createdAt());
    }
}
