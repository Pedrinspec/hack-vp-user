package com.example.authservice.auth.infrastructure.security;

import com.example.authservice.auth.domain.service.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BcryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }
}
