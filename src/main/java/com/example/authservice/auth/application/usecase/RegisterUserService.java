package com.example.authservice.auth.application.usecase;

import com.example.authservice.auth.application.dto.AuthResult;
import com.example.authservice.auth.application.dto.RegisterCommand;
import com.example.authservice.auth.application.exception.UserAlreadyExistsException;
import com.example.authservice.auth.application.port.in.RegisterUserUseCase;
import com.example.authservice.auth.application.port.out.PasswordHasherPort;
import com.example.authservice.auth.application.port.out.TokenGeneratorPort;
import com.example.authservice.auth.application.port.out.UserRepositoryPort;
import com.example.authservice.auth.domain.entity.User;
import com.example.authservice.auth.domain.vo.Email;

import java.util.Map;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenGeneratorPort tokenGenerator;

    public RegisterUserService(UserRepositoryPort userRepository,
                               PasswordHasherPort passwordHasher,
                               TokenGeneratorPort tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResult execute(RegisterCommand command) {
        var email = Email.of(command.email());
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new UserAlreadyExistsException("User already exists for email: " + email.value());
        });

        var user = User.create(email, passwordHasher.hash(command.rawPassword()));
        var saved = userRepository.save(user);

        var token = tokenGenerator.generate(saved.id().toString(), Map.of("email", saved.email().value()));
        return new AuthResult(token);
    }
}
