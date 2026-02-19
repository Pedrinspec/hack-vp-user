package com.example.authservice.auth.application.port.in;

import com.example.authservice.auth.application.dto.AuthResult;
import com.example.authservice.auth.application.dto.RegisterCommand;

public interface RegisterUserUseCase {
    AuthResult execute(RegisterCommand command);
}
