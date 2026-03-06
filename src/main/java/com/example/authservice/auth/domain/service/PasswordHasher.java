package com.example.authservice.auth.domain.service;

public interface PasswordHasher {
    String hash(String password);
}
