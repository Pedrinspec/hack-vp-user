package com.fiap.authservice.auth.application.dto;

public record LoginCommand(String email, String password) {
}
