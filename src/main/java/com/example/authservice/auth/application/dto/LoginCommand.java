package com.example.authservice.auth.application.dto;

public record LoginCommand(String email, String rawPassword) {
}
