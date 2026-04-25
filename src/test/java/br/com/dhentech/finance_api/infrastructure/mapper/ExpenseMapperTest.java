package br.com.dhentech.finance_api.infrastructure.mapper;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.*;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExpenseMapperTest {

    private final ExpenseMapper mapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    @DisplayName("Deve mapear corretamente o domínio para o response DTO")
    void shouldMapDomainToResponse() {
        Category category = new Category("Moradia", "Contas mensais");
        User user = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");

        Expense expense = new Expense(
                "Aluguel",
                new BigDecimal("1500.00"),
                LocalDate.now(),
                ExpenseType.RECURRING,
                user,
                category
        );

        ExpenseResponse response = mapper.toResponse(expense);

        assertNotNull(response);
        assertEquals(expense.getDescription(), response.description());
        assertEquals(expense.getAmount(), response.amount());
        assertEquals("Moradia", response.categoryName());
    }
}