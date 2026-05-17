package br.com.dhentech.finance_api.core.usecases.categories;

import br.com.dhentech.finance_api.application.dto.CategoryResponse;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public ListCategoriesUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> execute() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ))
                .toList();
    }
}