package com.fiap.authservice.application.usecase;

import com.fiap.authservice.application.dto.AuthRequest;
import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.repository.UserRepository;
import com.fiap.authservice.domain.service.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    void shouldCreateUserSuccessfullyWhenRequestIsValidAndEmailDoesNotExist() {

        // Arrange
        AuthRequest request = new AuthRequest("Maria Silva", "maria@fiap.com", "123456");
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .email(request.email())
                .password("hashed-password")
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.password())).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = createUserUseCase.execute(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Maria Silva");
        assertThat(result.getEmail()).isEqualTo("maria@fiap.com");
        assertThat(result.getPassword()).isEqualTo("hashed-password");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User userToSave = captor.getValue();

        assertThat(userToSave.getId()).isNotNull();
        assertThat(userToSave.getName()).isEqualTo("Maria Silva");
        assertThat(userToSave.getEmail()).isEqualTo("maria@fiap.com");
        assertThat(userToSave.getPassword()).isEqualTo("hashed-password");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        // Arrange
        AuthRequest request = new AuthRequest(null, "maria@fiap.com", "123456");

        // Act + Assert
        assertThatThrownBy(() -> createUserUseCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome é obrigatório");

        verify(userRepository, never()).findByEmail(any());
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {

        // Arrange
        AuthRequest request = new AuthRequest("   ", "maria@fiap.com", "123456");

        // Act + Assert
        assertThatThrownBy(() -> createUserUseCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome é obrigatório");

        verify(userRepository, never()).findByEmail(any());
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        AuthRequest request = new AuthRequest("Maria Silva", "maria@fiap.com", "123456");
        User existingUser = User.builder().id(UUID.randomUUID()).email(request.email()).build();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        // Act + Assert
        assertThatThrownBy(() -> createUserUseCase.execute(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("E-mail já cadastrado");

        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldAllowNameWithLeadingAndTrailingSpaces() {

        // Arrange
        AuthRequest request = new AuthRequest("  Maria Silva  ", "maria@fiap.com", "123456");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.password())).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = createUserUseCase.execute(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("  Maria Silva  ");
    }

    @Test
    void shouldPropagateExceptionWhenPasswordHasherFails() {

        // Arrange
        AuthRequest request = new AuthRequest("Maria Silva", "maria@fiap.com", "123456");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(request.password())).thenThrow(new IllegalStateException("Erro ao hashear senha"));

        // Act + Assert
        assertThatThrownBy(() -> createUserUseCase.execute(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Erro ao hashear senha");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenRequestIsNull() {

        // Arrange
        AuthRequest request = null;

        // Act + Assert
        assertThatThrownBy(() -> createUserUseCase.execute(request))
                .isInstanceOf(NullPointerException.class);

        verify(userRepository, never()).findByEmail(any());
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }
}
