package br.com.dhentech.finance_api.application.dto;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Due date is required")
        LocalDate dueDate,

        @NotNull(message = "Type is required")
        ExpenseType type,

        @NotNull(message = "Category is required")
        UUID categoryId
) {}
