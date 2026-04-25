package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.UserRegistrationRequest;
import br.com.dhentech.finance_api.application.dto.UserResponse;
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
import br.com.dhentech.finance_api.core.usecases.RegisterUserUseCase;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @DisplayName("Deve retornar 201 Created ao cadastrar um usuário com sucesso")
    void shouldReturn201WhenUserIsCreated() throws Exception {
        // 1. Cenário
        UserRegistrationRequest request = new UserRegistrationRequest("Denilson", "denilson@teste.com", "senha123");
        String jsonRequest = objectMapper.writeValueAsString(request);

        UUID generatedId = UUID.randomUUID();
        UserResponse mockResponse = new UserResponse(generatedId, "Denilson", "denilson@teste.com");


        when(registerUserUseCase.execute(any(UserRegistrationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.name").value("Denilson"));
    }
}