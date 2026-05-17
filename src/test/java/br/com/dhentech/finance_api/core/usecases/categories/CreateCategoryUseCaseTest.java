package br.com.dhentech.finance_api.core.usecases.categories;

import br.com.dhentech.finance_api.application.dto.CategoryRequest;
import br.com.dhentech.finance_api.application.dto.CategoryResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    @DisplayName("Deve criar uma categoria e retornar o DTO de resposta corretamente")
    void shouldCreateCategorySuccessfully() {
        CategoryRequest request = new CategoryRequest("Lazer", "Gastos com diversão");
        UUID expectedId = UUID.randomUUID();

        Category mockedSavedCategory = mock(Category.class);

        when(mockedSavedCategory.getId()).thenReturn(expectedId);
        when(mockedSavedCategory.getName()).thenReturn("Lazer");
        when(mockedSavedCategory.getDescription()).thenReturn("Gastos com diversão");

        when(categoryRepository.save(any(Category.class))).thenReturn(mockedSavedCategory);


        CategoryResponse response = createCategoryUseCase.execute(request);

        assertNotNull(response);
        assertEquals(expectedId, response.id());
        assertEquals("Lazer", response.name());
        assertEquals("Gastos com diversão", response.description());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}