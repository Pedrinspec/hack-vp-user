package com.example.authservice.auth.application.port.out;

import com.example.authservice.auth.domain.entity.User;
import com.example.authservice.auth.domain.vo.Email;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(Email email);
    User save(User user);
}
