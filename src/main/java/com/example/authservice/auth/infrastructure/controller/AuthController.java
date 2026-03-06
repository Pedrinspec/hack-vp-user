package com.example.authservice.auth.infrastructure.controller;

import com.example.authservice.auth.application.dto.AuthRequest;
import com.example.authservice.auth.application.dto.TokenResponse;
import com.example.authservice.auth.application.usecase.CreateUserUseCase;
import com.example.authservice.auth.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody AuthRequest request) {
        var user = createUserUseCase.execute(request);
        String token = tokenService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = tokenService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
