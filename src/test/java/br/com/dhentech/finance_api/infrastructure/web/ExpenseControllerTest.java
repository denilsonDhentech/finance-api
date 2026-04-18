package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.infrastructure.config.LocalIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@LocalIntegrationTest
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 e mensagem de erro ao tentar criar despesa com valor negativo")
    void shouldReturn400WhenAmountIsNegative() throws Exception {
        String json = """
                {
                    "description": "Pagamento Teste",
                    "amount": -10.50,
                    "dueDate": "2026-04-18",
                    "type": "ONE_TIME"
                }
                """;

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be positive"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
