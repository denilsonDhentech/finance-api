package br.com.dhentech.finance_api.infrastructure.security;

import br.com.dhentech.finance_api.core.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setup() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "minha-chave-de-teste-123");
    }

    @Test
    void shouldGenerateTokenCorrectly() {
        User user = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "hash123");

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldValidateTokenAndReturnEmail() {
        User user = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "hash123");
        String token = tokenService.generateToken(user);

        String email = tokenService.validateToken(token);

        assertEquals("denilson@teste.com", email);
    }

    @Test
    void shouldReturnEmptyStringWhenTokenIsInvalid() {
        String invalidToken = "token-totalmente-invalido.123.456";

        String email = tokenService.validateToken(invalidToken);

        assertEquals("", email);
    }
}