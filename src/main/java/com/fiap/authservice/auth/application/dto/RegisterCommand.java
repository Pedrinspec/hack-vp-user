package com.fiap.authservice.auth.application.dto;

public record RegisterCommand(String username, String email, String password) {
}
