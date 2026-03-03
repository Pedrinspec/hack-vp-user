package com.fiap.authservice.auth.application.service;

import com.fiap.authservice.auth.application.dto.AuthResult;
import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.dto.RegisterCommand;
import com.fiap.authservice.auth.application.exception.InvalidCredentialsException;
import com.fiap.authservice.auth.application.exception.UserAlreadyExistsException;
import com.fiap.authservice.auth.domain.entity.User;
import com.fiap.authservice.auth.infrastructure.security.JwtTokenService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuthApplicationService {

    private final JwtTokenService jwtTokenService;
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();

    public AuthApplicationService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    public AuthResult register(RegisterCommand command) {
        String normalizedEmail = command.email().toLowerCase(Locale.ROOT);
        String normalizedUsername = command.username().toLowerCase(Locale.ROOT);

        if (usersByEmail.containsKey(normalizedEmail) || usersByUsername.containsKey(normalizedUsername)) {
            throw new UserAlreadyExistsException();
        }

        User user = new User(command.username(), command.email(), hashPassword(command.password()));
        usersByEmail.put(normalizedEmail, user);
        usersByUsername.put(normalizedUsername, user);
        return new AuthResult(jwtTokenService.generate(user.username()));
    }

    public AuthResult login(LoginCommand command) {
        User user = findByIdentifier(command.identifier());
        if (user == null || !hashPassword(command.password()).equals(user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return new AuthResult(jwtTokenService.generate(user.username()));
    }

    private User findByIdentifier(String identifier) {
        String normalizedIdentifier = identifier.toLowerCase(Locale.ROOT);
        if (normalizedIdentifier.contains("@")) {
            return usersByEmail.get(normalizedIdentifier);
        }
        return usersByUsername.get(normalizedIdentifier);
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception exception) {
            throw new IllegalStateException("Falha ao processar senha", exception);
        }
    }
}
