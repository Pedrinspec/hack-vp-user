package com.fiap.authservice.auth.infrastructure.config;

import com.fiap.authservice.auth.application.port.in.LoginUseCase;
import com.fiap.authservice.auth.application.port.in.RegisterUserUseCase;
import com.fiap.authservice.auth.application.port.out.PasswordHasherPort;
import com.fiap.authservice.auth.application.port.out.TokenGeneratorPort;
import com.fiap.authservice.auth.application.port.out.UserRepositoryPort;
import com.fiap.authservice.auth.application.service.LoginService;
import com.fiap.authservice.auth.application.service.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthApplicationConfig {

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepositoryPort,
                                            PasswordHasherPort passwordHasherPort) {
        return new RegisterUserService(userRepositoryPort, passwordHasherPort);
    }

    @Bean
    LoginUseCase loginUseCase(UserRepositoryPort userRepositoryPort,
                              PasswordHasherPort passwordHasherPort,
                              TokenGeneratorPort tokenGeneratorPort) {
        return new LoginService(userRepositoryPort, passwordHasherPort, tokenGeneratorPort);
    }
}
