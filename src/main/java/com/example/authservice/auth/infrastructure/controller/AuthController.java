package com.example.authservice.auth.infrastructure.controller;

import com.example.authservice.auth.application.dto.LoginCommand;
import com.example.authservice.auth.application.dto.RegisterCommand;
import com.example.authservice.auth.application.port.in.LoginUseCase;
import com.example.authservice.auth.application.port.in.RegisterUserUseCase;
import com.example.authservice.auth.infrastructure.dto.AuthResponse;
import com.example.authservice.auth.infrastructure.dto.LoginRequest;
import com.example.authservice.auth.infrastructure.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        var result = registerUserUseCase.execute(new RegisterCommand(request.email(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(result.accessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        var result = loginUseCase.execute(new LoginCommand(request.email(), request.password()));
        return ResponseEntity.ok(new AuthResponse(result.accessToken()));
    }
}
