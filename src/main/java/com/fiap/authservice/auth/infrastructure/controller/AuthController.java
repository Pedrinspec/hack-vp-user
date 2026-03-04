package com.fiap.authservice.auth.infrastructure.controller;

import com.fiap.authservice.auth.application.dto.AuthResult;
import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.dto.RegisterCommand;
import com.fiap.authservice.auth.application.service.AuthApplicationService;
import com.fiap.authservice.auth.infrastructure.dto.AuthResponse;
import com.fiap.authservice.auth.infrastructure.dto.LoginRequest;
import com.fiap.authservice.auth.infrastructure.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = authApplicationService.register(
                new RegisterCommand(request.username(), request.email(), request.password())
        );
        return new AuthResponse(result.token(), "Bearer");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authApplicationService.login(new LoginCommand(request.identifier(), request.password()));
        return new AuthResponse(result.token(), "Bearer");
    }
}
