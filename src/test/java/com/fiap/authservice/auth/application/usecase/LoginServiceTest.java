package com.fiap.authservice.auth.application.usecase;

import com.fiap.authservice.auth.application.dto.AuthResult;
import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.exception.InvalidCredentialsException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordHasherPort passwordHasher;

    @Mock
    private TokenGeneratorPort tokenGenerator;

    @InjectMocks
    private LoginService loginService;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        User user = User.restore(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                Email.of("user@example.com"),
                "hashed",
                Instant.now()
        );

        when(userRepository.findByEmail(Email.of("user@example.com"))).thenReturn(Optional.of(user));
        when(passwordHasher.matches("raw-pass", "hashed")).thenReturn(true);
        when(tokenGenerator.generate(
                "00000000-0000-0000-0000-000000000001",
                Map.of("email", "user@example.com")
        )).thenReturn("jwt-token");

        AuthResult result = loginService.execute(new LoginCommand("user@example.com", "raw-pass"));

        assertEquals("jwt-token", result.accessToken());
        verify(passwordHasher).matches("raw-pass", "hashed");
        verify(tokenGenerator).generate(
                eq("00000000-0000-0000-0000-000000000001"),
                eq(Map.of("email", "user@example.com"))
        );
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByEmail(Email.of("missing@example.com"))).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.execute(new LoginCommand("missing@example.com", "raw-pass"))
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordDoesNotMatch() {
        User user = User.restore(UUID.randomUUID(), Email.of("user@example.com"), "hashed", Instant.now());
        when(userRepository.findByEmail(Email.of("user@example.com"))).thenReturn(Optional.of(user));
        when(passwordHasher.matches("wrong-pass", "hashed")).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.execute(new LoginCommand("user@example.com", "wrong-pass"))
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
