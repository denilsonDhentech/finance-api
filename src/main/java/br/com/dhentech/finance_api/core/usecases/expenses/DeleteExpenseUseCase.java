package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteExpenseUseCase {

    private final JpaExpenseRepository expenseRepository;

    public DeleteExpenseUseCase(JpaExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional
    public void execute(UUID expenseId, UUID userId) {
        ExpenseEntity expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada ou não pertence ao usuário logado"));

        expenseRepository.delete(expense);
    }
}