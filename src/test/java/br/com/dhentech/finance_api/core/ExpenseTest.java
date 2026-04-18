package br.com.dhentech.finance_api.core;

import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    @Test
    @DisplayName("Deve criar uma despesa com status pendente e ID gerado")
    void shouldCreateExpenseWithPendingStatus() {
        Expense expense = new Expense("Internet", new BigDecimal("100.00"), LocalDate.now(), ExpenseType.RECURRING);

        assertNotNull(expense.getId());
        assertEquals(ExpenseStatus.PENDING, expense.getStatus());
        assertEquals("Internet", expense.getDescription());
    }

    @Test
    @DisplayName("Deve marcar uma despesa como paga com sucesso")
    void shouldMarkAsPaid() {
        Expense expense = new Expense("Luz", new BigDecimal("150.00"), LocalDate.now(), ExpenseType.ONE_TIME);

        expense.markAsPaid();

        assertEquals(ExpenseStatus.PAID, expense.getStatus());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar pagar uma despesa já paga")
    void shouldThrowErrorWhenAlreadyPaid() {
        Expense expense = new Expense("Luz", new BigDecimal("150.00"), LocalDate.now(), ExpenseType.ONE_TIME);
        expense.markAsPaid();

        assertThrows(IllegalStateException.class, expense::markAsPaid);
    }

    @Test
    @DisplayName("Não deve permitir criar despesa com valor negativo")
    void shouldNotAllowNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                new Expense("Erro", new BigDecimal("-10.00"), LocalDate.now(), ExpenseType.ONE_TIME)
        );
    }
}
