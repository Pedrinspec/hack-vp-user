package com.example.authservice.auth.domain.repository;

import com.example.authservice.auth.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);
}
