package br.com.dhentech.finance_api.core.usecases.categories;

import br.com.dhentech.finance_api.application.dto.CategoryRequest;
import br.com.dhentech.finance_api.application.dto.CategoryResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public CreateCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse execute(CategoryRequest request) {
        Category category = new Category(request.name(), request.description());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription()
        );
    }
}