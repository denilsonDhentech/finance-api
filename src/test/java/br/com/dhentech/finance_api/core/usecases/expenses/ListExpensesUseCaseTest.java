package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.application.dto.ExpenseFilter;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.dto.PagedResponse;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

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
        ExpenseFilter emptyFilter = new ExpenseFilter(null, null, null);
        int page = 0;
        int size = 10;

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

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dueDate"));
        Page<ExpenseEntity> pagedResponse = new PageImpl<>(List.of(entity), pageable, 1);

        when(expenseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pagedResponse);

        when(expenseMapper.toResponse(any(ExpenseEntity.class))).thenReturn(expectedResponse);

        PagedResponse<ExpenseResponse> result = listExpensesUseCase.execute(emptyFilter, page, size, loggedUserId);

        assertNotNull(result, "A página não deveria ser nula");
        assertEquals(1, result.totalElements(), "A página deveria conter 1 item");
        assertEquals(0, result.page());
        assertEquals(10, result.size());

        ExpenseResponse response = result.content().get(0);
        assertEquals("Mercado", response.description());
        assertEquals(new BigDecimal("150.00"), response.amount());
        assertEquals("Alimentação", response.categoryName());

        verify(expenseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(expenseMapper, times(1)).toResponse(any(ExpenseEntity.class));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia caso o usuário não tenha despesas")
    void shouldReturnEmptyListWhenUserHasNoExpenses() {
        UUID loggedUserId = UUID.randomUUID();
        ExpenseFilter emptyFilter = new ExpenseFilter(null, null, null);
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dueDate"));
        Page<ExpenseEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(expenseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        PagedResponse<ExpenseResponse> result = listExpensesUseCase.execute(emptyFilter, page, size, loggedUserId);

        assertNotNull(result);
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.totalElements());

        verify(expenseMapper, never()).toResponse(any(ExpenseEntity.class));
    }
}