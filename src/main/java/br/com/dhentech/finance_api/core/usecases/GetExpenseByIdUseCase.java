package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetExpenseByIdUseCase {

    private final JpaExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public GetExpenseByIdUseCase(JpaExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    public ExpenseResponse execute(UUID expenseId, UUID userId) {
        ExpenseEntity expenseEntity = expenseRepository.findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada ou não pertence ao usuário logado"));

        return expenseMapper.toResponse(expenseEntity);
    }
}
