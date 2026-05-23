package br.com.dhentech.finance_api.core.usecases.expenses;

import br.com.dhentech.finance_api.application.dto.ExpenseFilter;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.dto.PagedResponse;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseSpecification;
import br.com.dhentech.finance_api.infrastructure.persistence.JpaExpenseRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

@Service
public class ListExpensesUseCase {

    private final JpaExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ListExpensesUseCase(JpaExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    public PagedResponse<ExpenseResponse> execute(ExpenseFilter filter, int page, int size, UUID userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dueDate"));

        Specification<ExpenseEntity> spec = Specification
                .where(ExpenseSpecification.belongsToUser(userId))
                .and(ExpenseSpecification.hasStartDate(filter.startDate()))
                .and(ExpenseSpecification.hasEndDate(filter.endDate()))
                .and(ExpenseSpecification.hasCategory(filter.categoryId()));

        Page<ExpenseEntity> expensePage = expenseRepository.findAll(spec, pageable);

        List<ExpenseResponse> content = expensePage.getContent()
                .stream()
                .map(expenseMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                expensePage.getNumber(),
                expensePage.getSize(),
                expensePage.getTotalElements(),
                expensePage.getTotalPages(),
                expensePage.isLast()
        );
    }
}