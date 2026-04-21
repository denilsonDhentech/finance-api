package br.com.dhentech.finance_api.mapper;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.User;

public class ExpenseMapper {

    private ExpenseMapper() {}

    public static Expense toDomain(ExpenseRequest request, User user, Category category) {
        return new Expense(
                request.description(),
                request.amount(),
                request.dueDate(),
                request.type(),
                user,
                category
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