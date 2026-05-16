package br.com.dhentech.finance_api.infrastructure.web.exceptions;

import br.com.dhentech.finance_api.core.exceptions.BusinessRuleException;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("Deve retornar 400 Bad Request para IllegalArgumentException")
    void shouldHandleIllegalArgument() {

        IllegalArgumentException exception = new IllegalArgumentException("Valor não pode ser negativo");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/expenses");

        ResponseEntity<StandardError> response = handler.handleIllegalArgument(exception, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals("Valor não pode ser negativo", response.getBody().message());
        assertEquals("/expenses", response.getBody().path());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Deve retornar 400 e lista de erros para MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("expenseRequest", "amount", "não deve ser nulo");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/expenses");

        ResponseEntity<StandardError> response = handler.handleValidationExceptions(exception, request);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de validação nos campos: amount: não deve ser nulo", response.getBody().message());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found para ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Categoria não encontrada para o ID informado");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/expenses");

        ResponseEntity<StandardError> response = handler.handleResourceNotFound(exception, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals("Categoria não encontrada para o ID informado", response.getBody().message());
        assertEquals("/api/expenses", response.getBody().path());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict para BusinessRuleException")
    void shouldHandleBusinessRuleException() {
        BusinessRuleException exception = new BusinessRuleException("Este e-mail já está em uso.");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/users");

        ResponseEntity<StandardError> response = handler.handleBusinessRule(exception, request);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
        assertEquals("Este e-mail já está em uso.", response.getBody().message());
        assertEquals("/api/users", response.getBody().path());
        assertNotNull(response.getBody().timestamp());
    }
}