package com.example.authservice.auth.infrastructure.jpa;

import com.example.authservice.auth.application.port.out.UserRepositoryPort;
import com.example.authservice.auth.domain.entity.User;
import com.example.authservice.auth.domain.vo.Email;
import com.example.authservice.auth.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.value()).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(repository.save(UserMapper.toJpa(user)));
    }
}
