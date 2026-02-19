package com.example.authservice.auth.application.dto;

public record RegisterCommand(String email, String rawPassword) {
}
