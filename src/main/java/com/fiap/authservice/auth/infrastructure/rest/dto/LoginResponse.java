package com.fiap.authservice.auth.infrastructure.rest.dto;

public record LoginResponse(String accessToken, String tokenType) {
}
