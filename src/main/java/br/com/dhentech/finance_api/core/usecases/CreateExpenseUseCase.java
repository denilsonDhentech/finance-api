package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateExpenseUseCase {

    private final ExpenseRepositoryPort repositoryPort;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CreateExpenseUseCase(ExpenseRepositoryPort repositoryPort,
                                CategoryRepository categoryRepository,
                                UserRepository userRepository) {
        this.repositoryPort = repositoryPort;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponse execute(ExpenseRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada para o ID informado"));

        User loggedUser = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhum usuário cadastrado no banco. Por favor, crie um usuário primeiro!"));

        Expense expense = ExpenseMapper.toDomain(request, loggedUser, category);

        Expense savedExpense = repositoryPort.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }
}