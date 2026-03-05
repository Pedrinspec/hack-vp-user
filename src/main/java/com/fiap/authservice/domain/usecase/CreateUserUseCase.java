package com.fiap.authservice.domain.usecase;

import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher; // Interface para abstrair o BCrypt

    public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String hashedPassword = passwordHasher.hash(rawPassword);
        User user = new User(UUID.randomUUID(), name, email, hashedPassword);
        return userRepository.save(user);
    }

}
