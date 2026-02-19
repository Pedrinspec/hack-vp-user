package com.example.authservice.auth.infrastructure.config;

import com.example.authservice.auth.application.port.in.LoginUseCase;
import com.example.authservice.auth.application.port.in.RegisterUserUseCase;
import com.example.authservice.auth.application.port.out.PasswordHasherPort;
import com.example.authservice.auth.application.port.out.TokenGeneratorPort;
import com.example.authservice.auth.application.port.out.UserRepositoryPort;
import com.example.authservice.auth.application.usecase.LoginService;
import com.example.authservice.auth.application.usecase.RegisterUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepositoryPort,
                                            PasswordHasherPort passwordHasherPort,
                                            TokenGeneratorPort tokenGeneratorPort) {
        return new RegisterUserService(userRepositoryPort, passwordHasherPort, tokenGeneratorPort);
    }

    @Bean
    LoginUseCase loginUseCase(UserRepositoryPort userRepositoryPort,
                              PasswordHasherPort passwordHasherPort,
                              TokenGeneratorPort tokenGeneratorPort) {
        return new LoginService(userRepositoryPort, passwordHasherPort, tokenGeneratorPort);
    }
}
