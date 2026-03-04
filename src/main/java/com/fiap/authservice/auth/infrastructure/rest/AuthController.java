package com.fiap.authservice.auth.infrastructure.rest;

import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.dto.LoginResult;
import com.fiap.authservice.auth.application.dto.RegisterUserCommand;
import com.fiap.authservice.auth.application.dto.RegisterUserResult;
import com.fiap.authservice.auth.application.port.in.LoginUseCase;
import com.fiap.authservice.auth.application.port.in.RegisterUserUseCase;
import com.fiap.authservice.auth.infrastructure.rest.dto.LoginRequest;
import com.fiap.authservice.auth.infrastructure.rest.dto.LoginResponse;
import com.fiap.authservice.auth.infrastructure.rest.dto.RegisterRequest;
import com.fiap.authservice.auth.infrastructure.rest.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserResult result = registerUserUseCase.execute(new RegisterUserCommand(request.email(), request.password()));
        return new RegisterResponse(result.userId(), result.email(), result.createdAt());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = loginUseCase.execute(new LoginCommand(request.email(), request.password()));
        return new LoginResponse(result.accessToken(), result.tokenType());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessError(IllegalArgumentException exception) {
        return exception.getMessage();
    }
}
