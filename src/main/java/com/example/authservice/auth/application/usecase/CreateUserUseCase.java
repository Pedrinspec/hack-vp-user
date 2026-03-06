package com.example.authservice.auth.application.usecase;

import com.example.authservice.auth.application.dto.AuthRequest;
import com.example.authservice.auth.domain.entity.User;
import com.example.authservice.auth.domain.repository.UserRepository;
import com.example.authservice.auth.domain.service.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public User execute(AuthRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        userRepository.findByEmail(request.email())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("E-mail já cadastrado");
                });

        User user = User.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .email(request.email())
                .password(passwordHasher.hash(request.password()))
                .build();

        return userRepository.save(user);
    }
}
