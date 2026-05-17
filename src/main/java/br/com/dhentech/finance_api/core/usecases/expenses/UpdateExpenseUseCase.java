package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateExpenseUseCase {

    private final JpaExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    public UpdateExpenseUseCase(JpaExpenseRepository expenseRepository,
                                CategoryRepository categoryRepository,
                                ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.expenseMapper = expenseMapper;
    }

    @Transactional
    public ExpenseResponse execute(UUID expenseId, ExpenseRequest request, UUID userId) {
        ExpenseEntity expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada ou não pertence ao usuário logado"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada para o ID informado"));

        ExpenseEntity updatedEntity = expense.copyWith(
                request.description(),
                request.amount(),
                request.dueDate(),
                request.type(),
                category
        );

        ExpenseEntity updatedExpense = expenseRepository.save(updatedEntity);
        return expenseMapper.toResponse(updatedExpense);
    }
}