package br.com.dhentech.finance_api.application.dto;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(
        String description,
        BigDecimal amount,
        LocalDate dueDate,
        ExpenseType type,
        UUID categoryId
) {}
