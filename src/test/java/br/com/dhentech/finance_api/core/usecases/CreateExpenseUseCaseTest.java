package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateExpenseUseCaseTest {

    @Mock
    private ExpenseRepositoryPort repositoryPort;

    @InjectMocks
    private CreateExpenseUseCase createExpenseUseCase;

    @Test
    void shouldCreateExpenseSuccessfully() {
        // Arrange
        ExpenseRequest request = new ExpenseRequest("Academia", new BigDecimal("120.00"), LocalDate.now(), ExpenseType.RECURRING);
        when(repositoryPort.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ExpenseResponse response = createExpenseUseCase.execute(request);

        // Assert
        assertNotNull(response.id());
        assertEquals("Academia", response.description());
        verify(repositoryPort, times(1)).save(any(Expense.class));
    }
}
