package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateExpenseUseCase {

    private final ExpenseRepositoryPort repositoryPort;

    public CreateExpenseUseCase(ExpenseRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public ExpenseResponse execute(ExpenseRequest request) {
        Expense expense = ExpenseMapper.toDomain(request);

        Expense savedExpense = repositoryPort.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }
}
