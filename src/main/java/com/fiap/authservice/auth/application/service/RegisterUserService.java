package com.fiap.authservice.auth.application.service;

import com.fiap.authservice.auth.application.dto.RegisterUserCommand;
import com.fiap.authservice.auth.application.dto.RegisterUserResult;
import com.fiap.authservice.auth.application.port.in.RegisterUserUseCase;
import com.fiap.authservice.auth.application.port.out.PasswordHasherPort;
import com.fiap.authservice.auth.application.port.out.UserRepositoryPort;
import com.fiap.authservice.auth.domain.model.User;
import com.fiap.authservice.auth.domain.valueobject.Email;

import java.util.Objects;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;

    public RegisterUserService(UserRepositoryPort userRepository, PasswordHasherPort passwordHasher) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
    }

    @Override
    public RegisterUserResult execute(RegisterUserCommand command) {
        Email email = new Email(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists");
        }

        String passwordHash = passwordHasher.hash(command.password());
        User created = userRepository.save(User.create(email, passwordHash));

        return new RegisterUserResult(created.getId(), created.getEmail().value(), created.getCreatedAt());
    }
}
