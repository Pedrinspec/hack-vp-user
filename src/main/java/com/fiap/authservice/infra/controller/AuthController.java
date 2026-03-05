package com.fiap.authservice.infra.controller;

import com.fiap.authservice.application.dto.AuthRequest;
import com.fiap.authservice.application.dto.TokenResponse;
import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.usecase.CreateUserUseCase;
import com.fiap.authservice.infra.security.provider.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CreateUserUseCase createUserUseCase;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          CreateUserUseCase createUserUseCase,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.createUserUseCase = createUserUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest data) {
        // O Use Case de Domínio cuida da lógica (validação, hash de senha)
        var user = createUserUseCase.execute(data.email(), data.email(), data.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Convertemos o Principal (UserDetails) para nossa entidade de domínio User
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(token));
    }

}
