package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.CategoryRequest;
import br.com.dhentech.finance_api.application.dto.CategoryResponse;
import br.com.dhentech.finance_api.core.usecases.categories.CreateCategoryUseCase;
import br.com.dhentech.finance_api.core.usecases.categories.ListCategoriesUseCase;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import br.com.dhentech.finance_api.infrastructure.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    @DisplayName("Deve retornar 201 Created ao criar uma nova categoria com sucesso")
    void shouldReturn201WhenCategoryIsCreated() throws Exception {
        CategoryRequest request = new CategoryRequest("Lazer", "Gastos com diversão, cinema e viagens");
        String jsonRequest = objectMapper.writeValueAsString(request);

        UUID generatedId = UUID.randomUUID();
        CategoryResponse mockResponse = new CategoryResponse(generatedId, "Lazer", "Gastos com diversão, cinema e viagens");

        when(createCategoryUseCase.execute(any(CategoryRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.name").value("Lazer"))
                .andExpect(jsonPath("$.description").value("Gastos com diversão, cinema e viagens"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma lista de categorias")
    void shouldReturn200AndCategoryList() throws Exception {
        CategoryResponse cat1 = new CategoryResponse(UUID.randomUUID(), "Lazer", "Gastos com diversão");
        CategoryResponse cat2 = new CategoryResponse(UUID.randomUUID(), "Educação", "Cursos e livros");
        List<CategoryResponse> mockList = List.of(cat1, cat2);

        when(listCategoriesUseCase.execute()).thenReturn(mockList);

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Lazer"))
                .andExpect(jsonPath("$[1].name").value("Educação"));
    }
}