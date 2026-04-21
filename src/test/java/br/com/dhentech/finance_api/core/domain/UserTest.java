package br.com.dhentech.finance_api.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Deve criar um utilizador válido quando os dados estiverem corretos")
    void shouldCreateUserWhenDataIsValid() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Denilson", "denilson@teste.com", "senha123");

        assertEquals("Denilson", user.getName());
        assertEquals("denilson@teste.com", user.getEmail());
        assertEquals("denilson@teste.com", user.getUsername());
        assertTrue(user.isEnabled());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email-invalido", "denilson@", "@teste.com", "denilson@teste."})
    @DisplayName("Deve lançar exceção quando o formato do e-mail for inválido")
    void shouldThrowExceptionWhenEmailIsInvalid(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () ->
                new User(UUID.randomUUID(), "Nome", invalidEmail, "senha123")
        );
    }
}