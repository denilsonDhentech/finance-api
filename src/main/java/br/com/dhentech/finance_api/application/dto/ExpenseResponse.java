package br.com.dhentech.finance_api.application.dto;

import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate dueDate,
        ExpenseType type,
        ExpenseStatus status,
        String categoryName
) {}
