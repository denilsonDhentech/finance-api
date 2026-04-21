package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.UserRegistrationRequest;
import br.com.dhentech.finance_api.application.dto.UserResponse;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private UserRegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserRegistrationRequest("Denilson", "denilson@teste.com", "senha123");
    }

    @Test
    @DisplayName("Deve registrar um usuário com sucesso, criptografando a senha")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        String encryptedPassword = "hash_da_senha_123";
        User savedUser = new User(UUID.randomUUID(), validRequest.name(), validRequest.email(), encryptedPassword);

        when(userRepository.findByEmail(validRequest.email())).thenReturn(null);
        when(passwordEncoder.encode(validRequest.password())).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse response = registerUserUseCase.execute(validRequest);

        // Assert
        assertNotNull(response.id());
        assertEquals("Denilson", response.name());
        assertEquals("denilson@teste.com", response.email());

        verify(userRepository, times(1)).findByEmail(validRequest.email());
        verify(passwordEncoder, times(1)).encode(validRequest.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um e-mail já existente")
    void shouldThrowExceptionWhenEmailIsDuplicated() {
        // Arrange
        User existingUser = new User(UUID.randomUUID(), "Outro Cara", validRequest.email(), "outrasenha");
        when(userRepository.findByEmail(validRequest.email())).thenReturn(existingUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registerUserUseCase.execute(validRequest);
        });

        assertEquals("Este e-mail já está em uso.", exception.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}