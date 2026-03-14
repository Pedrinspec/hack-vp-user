package com.fiap.authservice.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BcryptPasswordHasherTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BcryptPasswordHasher bcryptPasswordHasher;

    @Test
    void shouldHashPasswordUsingPasswordEncoder() {
        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        String hashedPassword = bcryptPasswordHasher.hash("plain");

        assertEquals("encoded", hashedPassword);
        verify(passwordEncoder).encode("plain");
    }
}
