package br.com.dhentech.finance_api.application.ports;

import br.com.dhentech.finance_api.core.domain.Expense;

public interface ExpenseRepositoryPort {
    Expense save(Expense expense);
}
