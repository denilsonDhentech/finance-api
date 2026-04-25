package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Expense;
import org.springframework.stereotype.Component;


@Component
public class ExpenseRepositoryAdapter implements ExpenseRepositoryPort {

    private final JpaExpenseRepository jpaRepository;

    public ExpenseRepositoryAdapter(JpaExpenseRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Expense save(Expense expense) {
        ExpenseEntity entity = new ExpenseEntity(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDueDate(),
                expense.getType(),
                expense.getStatus(),
                expense.getUser(),
                expense.getCategory()
        );

        jpaRepository.save(entity);

        return expense;
    }
}
