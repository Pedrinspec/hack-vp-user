package com.fiap.authservice.auth.infrastructure.jpa;

import com.fiap.authservice.auth.application.port.out.UserRepositoryPort;
import com.fiap.authservice.auth.domain.model.User;
import com.fiap.authservice.auth.domain.valueobject.Email;
import com.fiap.authservice.auth.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEmail(Email email) {
        return repository.existsByEmail(email.value());
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = repository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.value()).map(UserMapper::toDomain);
    }
}
