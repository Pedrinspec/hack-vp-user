package com.fiap.authservice.domain.repository;

import com.fiap.authservice.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);
}
