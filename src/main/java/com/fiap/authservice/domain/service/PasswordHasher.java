package com.fiap.authservice.domain.service;

public interface PasswordHasher {
    String hash(String password);
}
