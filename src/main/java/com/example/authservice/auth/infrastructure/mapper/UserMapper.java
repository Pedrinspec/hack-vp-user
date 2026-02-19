package com.example.authservice.auth.infrastructure.mapper;

import com.example.authservice.auth.domain.entity.User;
import com.example.authservice.auth.domain.vo.Email;
import com.example.authservice.auth.infrastructure.jpa.UserJpaEntity;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserJpaEntity toJpa(User user) {
        return new UserJpaEntity(user.id(), user.email().value(), user.passwordHash(), user.createdAt());
    }

    public static User toDomain(UserJpaEntity entity) {
        return User.restore(entity.getId(), Email.of(entity.getEmail()), entity.getPasswordHash(), entity.getCreatedAt());
    }
}
