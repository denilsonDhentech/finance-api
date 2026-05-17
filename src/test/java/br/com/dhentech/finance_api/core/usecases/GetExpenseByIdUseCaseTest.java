package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.core.usecases.expenses.GetExpenseByIdUseCase;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetExpenseByIdUseCaseTest {

    @Mock
    private JpaExpenseRepository expenseRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private GetExpenseByIdUseCase getExpenseByIdUseCase;

    @Test
    @DisplayName("Deve retornar a despesa quando ela for encontrada no banco e pertencer ao usuário")
    void shouldReturnExpenseWhenFound() {
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ExpenseEntity mockEntity = mock(ExpenseEntity.class);
        ExpenseResponse mockResponse = new ExpenseResponse(expenseId, "Conta de Luz", BigDecimal.TEN, LocalDate.now(), ExpenseType.ONE_TIME, ExpenseStatus.PENDING, "Moradia");

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.of(mockEntity));
        when(expenseMapper.toResponse(mockEntity)).thenReturn(mockResponse);

        ExpenseResponse result = getExpenseByIdUseCase.execute(expenseId, userId);

        assertNotNull(result);
        assertEquals(expenseId, result.id());
        assertEquals("Conta de Luz", result.description());

        verify(expenseRepository, times(1)).findByIdAndUser_Id(expenseId, userId);
        verify(expenseMapper, times(1)).toResponse(mockEntity);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a despesa não existir ou for de outro usuário")
    void shouldThrowExceptionWhenExpenseNotFound() {
        // Arrange
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> getExpenseByIdUseCase.execute(expenseId, userId));

        assertEquals("Despesa não encontrada ou não pertence ao usuário logado", exception.getMessage());

        verify(expenseRepository, times(1)).findByIdAndUser_Id(expenseId, userId);
        verifyNoInteractions(expenseMapper);
    }
}