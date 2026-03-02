package com.fiap.authservice.auth.application.service;

import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.dto.LoginResult;
import com.fiap.authservice.auth.application.port.in.LoginUseCase;
import com.fiap.authservice.auth.application.port.out.PasswordHasherPort;
import com.fiap.authservice.auth.application.port.out.TokenGeneratorPort;
import com.fiap.authservice.auth.application.port.out.UserRepositoryPort;
import com.fiap.authservice.auth.domain.model.User;
import com.fiap.authservice.auth.domain.valueobject.Email;

import java.util.Objects;

public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenGeneratorPort tokenGenerator;

    public LoginService(UserRepositoryPort userRepository,
                        PasswordHasherPort passwordHasher,
                        TokenGeneratorPort tokenGenerator) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator);
    }

    @Override
    public LoginResult execute(LoginCommand command) {
        User user = userRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        boolean valid = passwordHasher.matches(command.password(), user.getPasswordHash());
        if (!valid) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new LoginResult(tokenGenerator.generate(user), "Bearer");
    }
}
