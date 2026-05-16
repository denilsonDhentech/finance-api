package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateExpenseUseCase {

    private final ExpenseRepositoryPort repositoryPort;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    public CreateExpenseUseCase(ExpenseRepositoryPort repositoryPort,
                                CategoryRepository categoryRepository,
                                UserRepository userRepository,
                                ExpenseMapper expenseMapper) {
        this.repositoryPort = repositoryPort;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.expenseMapper = expenseMapper;
    }

    public ExpenseResponse execute(ExpenseRequest request, UUID loggedUserId) {

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada para o ID informado"));

        User loggedUser = userRepository.findById(loggedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado ou não autorizado"));

        Expense expense = new Expense(
                request.description(),
                request.amount(),
                request.dueDate(),
                request.type(),
                loggedUser,
                category
        );

        Expense savedExpense = repositoryPort.save(expense);
        return expenseMapper.toResponse(savedExpense);
    }
}