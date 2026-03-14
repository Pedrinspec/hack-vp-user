package com.fiap.authservice.infrastructure.security;

import com.fiap.authservice.domain.entity.User;
import com.fiap.authservice.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DomainUserDetailsService domainUserDetailsService;

    @Test
    void shouldLoadUserByEmail() {
        User user = new User(UUID.randomUUID(), "Maria", "maria@email.com", "hashed");
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(user));

        User loadedUser = (User) domainUserDetailsService.loadUserByUsername("maria@email.com");

        assertEquals(user, loadedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> domainUserDetailsService.loadUserByUsername("maria@email.com"));

        assertEquals("maria@email.com", exception.getMessage());
    }
}
