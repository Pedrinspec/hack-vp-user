package com.example.authservice.auth.application.usecase;

import com.example.authservice.auth.application.dto.AuthResult;
import com.example.authservice.auth.application.dto.LoginCommand;
import com.example.authservice.auth.application.exception.InvalidCredentialsException;
import com.example.authservice.auth.application.port.in.LoginUseCase;
import com.example.authservice.auth.application.port.out.PasswordHasherPort;
import com.example.authservice.auth.application.port.out.TokenGeneratorPort;
import com.example.authservice.auth.application.port.out.UserRepositoryPort;
import com.example.authservice.auth.domain.vo.Email;

import java.util.Map;

public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenGeneratorPort tokenGenerator;

    public LoginService(UserRepositoryPort userRepository,
                        PasswordHasherPort passwordHasher,
                        TokenGeneratorPort tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResult execute(LoginCommand command) {
        var email = Email.of(command.email());
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordHasher.matches(command.rawPassword(), user.passwordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        var token = tokenGenerator.generate(user.id().toString(), Map.of("email", user.email().value()));
        return new AuthResult(token);
    }
}
