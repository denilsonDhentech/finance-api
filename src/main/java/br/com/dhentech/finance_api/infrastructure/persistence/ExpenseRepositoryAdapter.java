package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Component;


@Component
public class ExpenseRepositoryAdapter implements ExpenseRepositoryPort {

    private final JpaExpenseRepository jpaRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseRepositoryAdapter(JpaExpenseRepository jpaRepository, ExpenseMapper expenseMapper) {
        this.jpaRepository = jpaRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public Expense save(Expense expense) {
        ExpenseEntity entity = expenseMapper.toEntity(expense);
        jpaRepository.save(entity);
        return expense;
    }
}
