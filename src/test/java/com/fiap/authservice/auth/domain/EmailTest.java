package com.fiap.authservice.auth.domain;

import com.fiap.authservice.auth.domain.vo.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void shouldNormalizeEmailToLowercaseAndTrim() {
        Email email = Email.of("  User.Name+tag@Example.COM  ");

        assertEquals("user.name+tag@example.com", email.value());
    }

    @Test
    void shouldRejectBlankEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Email.of("   ")
        );

        assertEquals("Email cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldRejectInvalidEmailFormat() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Email.of("invalid-email")
        );

        assertEquals("Invalid email format", exception.getMessage());
    }
}
