package br.com.dhentech.finance_api.core.usecases.categories;

import br.com.dhentech.finance_api.application.dto.CategoryResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCategoriesUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    @DisplayName("Deve listar todas as categorias disponíveis mapeadas para DTO")
    void shouldListCategoriesSuccessfully() {
        Category cat1 = mock(Category.class);
        when(cat1.getId()).thenReturn(UUID.randomUUID());
        when(cat1.getName()).thenReturn("Educação");
        when(cat1.getDescription()).thenReturn("Estudos");

        Category cat2 = mock(Category.class);
        when(cat2.getId()).thenReturn(UUID.randomUUID());
        when(cat2.getName()).thenReturn("Saúde");
        when(cat2.getDescription()).thenReturn("Médico");

        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));

        List<CategoryResponse> responses = listCategoriesUseCase.execute();

        assertEquals(2, responses.size());
        assertEquals("Educação", responses.get(0).name());
        assertEquals("Saúde", responses.get(1).name());

        verify(categoryRepository, times(1)).findAll();
    }
}