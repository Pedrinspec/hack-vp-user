package com.fiap.authservice.auth.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.authservice.auth.application.dto.AuthResult;
import com.fiap.authservice.auth.application.dto.LoginCommand;
import com.fiap.authservice.auth.application.exception.InvalidCredentialsException;
import com.fiap.authservice.auth.application.port.in.LoginUseCase;
import com.fiap.authservice.auth.application.port.in.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(loginUseCase.execute(any(LoginCommand.class))).thenReturn(new AuthResult("token-123"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginPayload("user@example.com", "123456")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token-123"));
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        when(loginUseCase.execute(any(LoginCommand.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginPayload("user@example.com", "wrong")
                        )))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginPayload("", "")
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid request payload"));
    }

    private record LoginPayload(String email, String password) {
    }
}
