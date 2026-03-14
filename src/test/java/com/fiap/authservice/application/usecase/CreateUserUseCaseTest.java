package com.fiap.authservice.application.usecase;

import com.fiap.authservice.application.dto.AuthRequest;
import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.repository.UserRepository;
import com.fiap.authservice.domain.service.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void shouldCreateUserWhenRequestIsValid() {
        AuthRequest request = new AuthRequest("Maria", "maria@email.com", "123456");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.password())).thenReturn("hashed");
        when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = createUserUseCase.execute(request);

        assertNotNull(createdUser.getId());
        assertEquals("Maria", createdUser.getName());
        assertEquals("maria@email.com", createdUser.getEmail());
        assertEquals("hashed", createdUser.getPassword());
        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).hash(request.password());
        verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        AuthRequest request = new AuthRequest(" ", "maria@email.com", "123456");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createUserUseCase.execute(request));

        assertEquals("Nome é obrigatório", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        AuthRequest request = new AuthRequest("Maria", "maria@email.com", "123456");
        User existing = User.builder().email(request.email()).build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createUserUseCase.execute(request));

        assertEquals("E-mail já cadastrado", exception.getMessage());
    }
}
