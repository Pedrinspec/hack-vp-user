package com.fiap.authservice.auth.domain.entity;

public record User(String username, String email, String passwordHash) {
}
