package com.fiap.authservice.auth.application.usecase;

import com.fiap.authservice.auth.application.dto.AuthResult;
import com.fiap.authservice.auth.application.dto.RegisterCommand;
import com.fiap.authservice.auth.application.exception.UserAlreadyExistsException;
import com.fiap.authservice.auth.application.port.out.PasswordHasherPort;
import com.fiap.authservice.auth.application.port.out.TokenGeneratorPort;
import com.fiap.authservice.auth.application.port.out.UserRepositoryPort;
import com.fiap.authservice.auth.domain.entity.User;
import com.fiap.authservice.auth.domain.vo.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordHasherPort passwordHasher;

    @Mock
    private TokenGeneratorPort tokenGenerator;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    void shouldRegisterNewUserAndReturnToken() {
        when(userRepository.findByEmail(Email.of("new.user@example.com"))).thenReturn(Optional.empty());
        when(passwordHasher.hash("plain-pass")).thenReturn("hashed-pass");

        User saved = User.restore(
                UUID.fromString("00000000-0000-0000-0000-000000000100"),
                Email.of("new.user@example.com"),
                "hashed-pass",
                Instant.now()
        );
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(tokenGenerator.generate(
                "00000000-0000-0000-0000-000000000100",
                Map.of("email", "new.user@example.com")
        )).thenReturn("generated-token");

        AuthResult result = registerUserService.execute(
                new RegisterCommand("new.user@example.com", "plain-pass")
        );

        assertEquals("generated-token", result.accessToken());
        verify(passwordHasher).hash("plain-pass");
        verify(tokenGenerator).generate(
                eq("00000000-0000-0000-0000-000000000100"),
                eq(Map.of("email", "new.user@example.com"))
        );
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        User existing = User.restore(UUID.randomUUID(), Email.of("existing@example.com"), "hash", Instant.now());
        when(userRepository.findByEmail(Email.of("existing@example.com"))).thenReturn(Optional.of(existing));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> registerUserService.execute(new RegisterCommand("existing@example.com", "plain-pass"))
        );

        assertEquals("User already exists with email: existing@example.com", exception.getMessage());
    }
}
