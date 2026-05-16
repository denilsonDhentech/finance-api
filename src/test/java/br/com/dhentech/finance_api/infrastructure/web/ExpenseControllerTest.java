package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.core.exceptions.ResourceNotFoundException;
import br.com.dhentech.finance_api.core.usecases.CreateExpenseUseCase;
import br.com.dhentech.finance_api.core.usecases.GetExpenseByIdUseCase;
import br.com.dhentech.finance_api.infrastructure.config.LocalIntegrationTest;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@LocalIntegrationTest
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CreateExpenseUseCase createExpenseUseCase;

    @MockitoBean
    private GetExpenseByIdUseCase getExpenseByIdUseCase;

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar despesa com valor negativo")
    void shouldReturn400WhenAmountIsNegative() throws Exception {
        UUID fakeCategoryId = UUID.randomUUID();
        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new Category("Alimentação", "Despesas")));

        User fakeUser = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");

        when(userRepository.findById(any())).thenReturn(Optional.of(fakeUser));
        when(userRepository.findAll()).thenReturn(List.of(fakeUser));

        ExpenseRequest request = new ExpenseRequest(
                "Pagamento Teste",
                new BigDecimal("-10.50"),
                LocalDate.parse("2026-04-18"),
                ExpenseType.ONE_TIME,
                fakeCategoryId
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/expenses")
                        .with(user(fakeUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando os dados da despesa forem inválidos")
    void shouldReturn400WhenExpenseDataIsInvalid() throws Exception {
        User fakeUser = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");

        ExpenseRequest invalidRequest = new ExpenseRequest("", new BigDecimal("-10.0"), null, null, null);
        String jsonRequest = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/expenses")
                        .with(user(fakeUser))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createExpenseUseCase);
    }

    @Test
    @DisplayName("Deve retornar 200 e os detalhes da despesa ao buscar por ID")
    void shouldReturn200WhenGetExpenseById() throws Exception {

        UUID expenseId = UUID.randomUUID();
        User fakeUser = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");


        ExpenseResponse mockResponse = new ExpenseResponse(
                expenseId,
                "Teclado Mecânico",
                new BigDecimal("350.00"),
                LocalDate.now(),
                ExpenseType.ONE_TIME,
                ExpenseStatus.PENDING,
                "Eletrônicos"
        );

        when(getExpenseByIdUseCase.execute(expenseId, fakeUser.getId())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/expenses/{id}", expenseId)
                        .with(user(fakeUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseId.toString()))
                .andExpect(jsonPath("$.description").value("Teclado Mecânico"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando a despesa não existir ou for de outro usuário")
    void shouldReturn404WhenExpenseNotFound() throws Exception {
        // Cenário
        UUID expenseId = UUID.randomUUID();
        User fakeUser = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");

        when(getExpenseByIdUseCase.execute(expenseId, fakeUser.getId()))
                .thenThrow(new ResourceNotFoundException("Despesa não encontrada"));

        mockMvc.perform(get("/api/expenses/{id}", expenseId)
                        .with(user(fakeUser)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Despesa não encontrada"));
    }
}