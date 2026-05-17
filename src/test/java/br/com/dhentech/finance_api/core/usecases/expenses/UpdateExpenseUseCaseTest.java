package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateExpenseUseCaseTest {

    @Mock
    private JpaExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private UpdateExpenseUseCase updateExpenseUseCase;

    @Test
    @DisplayName("Deve atualizar a despesa utilizando o copyWith com sucesso")
    void shouldUpdateExpenseSuccessfully() {
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ExpenseRequest request = new ExpenseRequest("Nova Descrição", BigDecimal.valueOf(200), LocalDate.now(), ExpenseType.ONE_TIME, categoryId);

        Category mockCategory = mock(Category.class);

        ExpenseEntity mockOldEntity = mock(ExpenseEntity.class);
        ExpenseEntity mockNewEntity = mock(ExpenseEntity.class);
        ExpenseResponse mockResponse = new ExpenseResponse(expenseId, "Nova Descrição", BigDecimal.valueOf(200), LocalDate.now(), ExpenseType.ONE_TIME, ExpenseStatus.PENDING, "Moradia");

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.of(mockOldEntity));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        when(mockOldEntity.copyWith(request.description(), request.amount(), request.dueDate(), request.type(), mockCategory))
                .thenReturn(mockNewEntity);

        when(expenseRepository.save(mockNewEntity)).thenReturn(mockNewEntity);
        when(expenseMapper.toResponse(mockNewEntity)).thenReturn(mockResponse);

        ExpenseResponse response = updateExpenseUseCase.execute(expenseId, request, userId);

        assertEquals("Nova Descrição", response.description());
        assertEquals(BigDecimal.valueOf(200), response.amount());
        verify(expenseRepository, times(1)).save(mockNewEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção se a categoria informada não existir")
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID expenseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ExpenseRequest request = new ExpenseRequest("Desc", BigDecimal.TEN, LocalDate.now(), ExpenseType.ONE_TIME, categoryId);
        ExpenseEntity mockOldEntity = mock(ExpenseEntity.class);

        when(expenseRepository.findByIdAndUser_Id(expenseId, userId)).thenReturn(Optional.of(mockOldEntity));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateExpenseUseCase.execute(expenseId, request, userId));

        verify(expenseRepository, never()).save(any());
    }
}