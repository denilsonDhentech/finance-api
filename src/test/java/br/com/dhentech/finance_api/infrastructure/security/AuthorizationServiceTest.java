package br.com.dhentech.finance_api.infrastructure.security;

import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        User user = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "hash123");
        when(userRepository.findByEmail("denilson@teste.com")).thenReturn(user);

        UserDetails result = authorizationService.loadUserByUsername("denilson@teste.com");

        assertNotNull(result);
        assertEquals("denilson@teste.com", result.getUsername());
        verify(userRepository, times(1)).findByEmail("denilson@teste.com");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("inexistente@teste.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                authorizationService.loadUserByUsername("inexistente@teste.com")
        );
    }
}