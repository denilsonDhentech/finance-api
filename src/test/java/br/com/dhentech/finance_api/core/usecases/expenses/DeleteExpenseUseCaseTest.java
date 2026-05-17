package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteExpenseUseCaseTest {

    @Mock
    private JpaExpenseRepository expenseRepository;

    @InjectMocks
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Test
    @DisplayName("Deve deletar a despesa quando ela existir e pertencer ao usuário logado")
    void shouldDeleteExpenseSuccessfully() {
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ExpenseEntity mockEntity = mock(ExpenseEntity.class);

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.of(mockEntity));

        deleteExpenseUseCase.execute(expenseId, userId);

        verify(expenseRepository, times(1)).findByIdAndUser_Id(expenseId, userId);
        verify(expenseRepository, times(1)).delete(mockEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar despesa inexistente ou sem permissão")
    void shouldThrowExceptionWhenExpenseNotFound() {
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deleteExpenseUseCase.execute(expenseId, userId));

        assertEquals("Despesa não encontrada ou não pertence ao usuário logado", exception.getMessage());
        verify(expenseRepository, times(1)).findByIdAndUser_Id(expenseId, userId);
        verify(expenseRepository, never()).delete(any());
    }
}