package com.fiap.authservice.infra.repository;

import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.repository.UserRepository;
import com.fiap.authservice.infra.persistence.UserEntity;
import com.fiap.authservice.infra.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryImpl jpaUserRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        return jpaUserRepository.save(entity.toDomain());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

}
