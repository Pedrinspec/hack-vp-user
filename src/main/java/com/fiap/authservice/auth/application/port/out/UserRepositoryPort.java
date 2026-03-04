package com.fiap.authservice.auth.application.port.out;

import com.fiap.authservice.auth.domain.model.User;
import com.fiap.authservice.auth.domain.valueobject.Email;

import java.util.Optional;

public interface UserRepositoryPort {

    boolean existsByEmail(Email email);

    User save(User user);

    Optional<User> findByEmail(Email email);
}
