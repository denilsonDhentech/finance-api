package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListExpensesUseCaseTest {

    @Mock
    private JpaExpenseRepository expenseRepository;

    @InjectMocks
    private ListExpensesUseCase listExpensesUseCase;

    @Mock
    private ExpenseMapper expenseMapper;

    @Test
    @DisplayName("Deve retornar uma página de despesas (ExpenseResponse) pertencentes ao usuário")
    void shouldReturnExpenseListForGivenUser() {
        UUID loggedUserId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Category mockCategory = new Category("Alimentação", "Compras de mercado");

        ExpenseEntity entity = new ExpenseEntity(
                UUID.randomUUID(),
                "Mercado",
                new BigDecimal("150.00"),
                LocalDate.of(2026, 5, 10),
                ExpenseType.ONE_TIME,
                ExpenseStatus.PENDING,
                null,
                mockCategory
        );

        ExpenseResponse expectedResponse = new ExpenseResponse(
                entity.getId(), "Mercado", new BigDecimal("150.00"),
                LocalDate.of(2026, 5, 10), ExpenseType.ONE_TIME, ExpenseStatus.PENDING, "Alimentação"
        );

        Page<ExpenseEntity> pagedResponse = new PageImpl<>(List.of(entity));

        when(expenseRepository.findAllByUser_IdOrderByDueDateDesc(eq(loggedUserId), any(Pageable.class)))
                .thenReturn(pagedResponse);

        when(expenseMapper.toResponse(any(ExpenseEntity.class))).thenReturn(expectedResponse);

        Page<ExpenseResponse> result = listExpensesUseCase.execute(loggedUserId, pageable);

        assertNotNull(result, "A página não deveria ser nula");
        assertEquals(1, result.getTotalElements(), "A página deveria conter 1 item");

        ExpenseResponse response = result.getContent().get(0);
        assertEquals("Mercado", response.description());
        assertEquals(new BigDecimal("150.00"), response.amount());
        assertEquals("Alimentação", response.categoryName());

        verify(expenseRepository, times(1)).findAllByUser_IdOrderByDueDateDesc(eq(loggedUserId), any(Pageable.class));
        verify(expenseMapper, times(1)).toResponse(any(ExpenseEntity.class));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia caso o usuário não tenha despesas")
    void shouldReturnEmptyListWhenUserHasNoExpenses() {
        UUID loggedUserId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ExpenseEntity> emptyPage = new PageImpl<>(List.of());

        when(expenseRepository.findAllByUser_IdOrderByDueDateDesc(eq(loggedUserId), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<ExpenseResponse> result = listExpensesUseCase.execute(loggedUserId, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(expenseMapper, never()).toResponse(any(ExpenseEntity.class));
    }
}