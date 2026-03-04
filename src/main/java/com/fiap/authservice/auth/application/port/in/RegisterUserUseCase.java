package com.fiap.authservice.auth.application.port.in;

import com.fiap.authservice.auth.application.dto.RegisterUserCommand;
import com.fiap.authservice.auth.application.dto.RegisterUserResult;

public interface RegisterUserUseCase {

    RegisterUserResult execute(RegisterUserCommand command);
}
