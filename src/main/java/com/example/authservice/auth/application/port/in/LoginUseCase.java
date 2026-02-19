package com.example.authservice.auth.application.port.in;

import com.example.authservice.auth.application.dto.AuthResult;
import com.example.authservice.auth.application.dto.LoginCommand;

public interface LoginUseCase {
    AuthResult execute(LoginCommand command);
}
