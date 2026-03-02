package com.fiap.authservice.auth.infrastructure.mapper;

import com.fiap.authservice.auth.domain.model.User;
import com.fiap.authservice.auth.domain.valueobject.Email;
import com.fiap.authservice.auth.infrastructure.jpa.UserJpaEntity;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail().value());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }

    public static User toDomain(UserJpaEntity entity) {
        return User.rehydrate(
                entity.getId(),
                new Email(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getCreatedAt()
        );
    }
}
