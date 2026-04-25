package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListExpensesUseCase {

    private final JpaExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ListExpensesUseCase(JpaExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    public Page<ExpenseResponse> execute(UUID userId, Pageable pageable) {
        return expenseRepository.findAllByUser_IdOrderByDueDateDesc(userId, pageable)
                .map(expenseMapper::toResponse);
    }
}