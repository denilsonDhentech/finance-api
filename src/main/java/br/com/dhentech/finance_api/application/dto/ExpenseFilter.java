package br.com.dhentech.finance_api.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ExpenseFilter(
        UUID categoryId,
        LocalDate startDate,
        LocalDate endDate
) {
}
