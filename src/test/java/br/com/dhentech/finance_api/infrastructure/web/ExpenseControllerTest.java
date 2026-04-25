package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


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

    @Test
    @DisplayName("Deve retornar 400 e mensagem de erro ao tentar criar despesa com valor negativo")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be positive"));
    }
}