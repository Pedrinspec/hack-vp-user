package com.fiap.authservice.auth.application.port.in;

import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.dto.LoginResult;

public interface LoginUseCase {

    LoginResult execute(LoginCommand command);
}
