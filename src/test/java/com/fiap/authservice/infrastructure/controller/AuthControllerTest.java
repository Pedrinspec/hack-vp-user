package com.fiap.authservice.infrastructure.controller;

import com.fiap.authservice.application.dto.AuthRequest;
import com.fiap.authservice.application.dto.TokenResponse;
import com.fiap.authservice.application.usecase.CreateUserUseCase;
import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.infrastructure.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldLoginAndReturnTokenWhenCredentialsAreValid() {
        AuthRequest request = new AuthRequest(null, "user@email.com", "123456");
        User principal = new User(UUID.randomUUID(), "nome", "user@email.com", "123456");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(tokenService.generateToken(principal)).thenReturn("jwt-token");

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().token());
        verify(authenticationManager).authenticate(any());
        verify(tokenService).generateToken(principal);
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        AuthRequest request = new AuthRequest(null, "user@email.com", "wrong");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("invalid"));

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
        verify(tokenService, never()).generateToken(any(User.class));
    }
}
