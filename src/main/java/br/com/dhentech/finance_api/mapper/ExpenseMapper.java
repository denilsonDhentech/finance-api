package br.com.dhentech.finance_api.mapper;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Expense;

public class ExpenseMapper {

    private ExpenseMapper() {}

    public static Expense toDomain(ExpenseRequest request) {
        return new Expense(
                request.description(),
                request.amount(),
                request.dueDate(),
                request.type()
        );
    }

    public static ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDueDate(),
                expense.getType(),
                expense.getStatus()
        );
    }
}
