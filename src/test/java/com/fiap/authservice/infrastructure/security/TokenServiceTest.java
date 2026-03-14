package com.fiap.authservice.infrastructure.security;

import com.fiap.authservice.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "my-secret-for-tests");
        ReflectionTestUtils.setField(tokenService, "issuer", "auth-service-test");
    }

    @Test
    void shouldGenerateAndValidateToken() {
        User user = new User(UUID.randomUUID(), "Maria", "maria@email.com", "hashed");

        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);

        assertNotNull(token);
        assertEquals(user.getId().toString(), subject);
    }

    @Test
    void shouldReturnEmailFromToken() {
        User user = new User(UUID.randomUUID(), "Maria", "maria@email.com", "hashed");

        String token = tokenService.generateToken(user);

        assertEquals("maria@email.com", tokenService.validateTokenEmail(token));
    }

    @Test
    void shouldReturnNullWhenTokenIsInvalid() {
        assertNull(tokenService.validateToken("invalid-token"));
        assertNull(tokenService.validateTokenEmail("invalid-token"));
    }
}
