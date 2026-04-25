package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.AuthenticationRequest;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import br.com.dhentech.finance_api.infrastructure.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar 200 OK e o Token quando o login for feito com sucesso")
    void shouldReturn200AndTokenWhenLoginIsSuccessful() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("denilson@teste.com", "senha123");
        String jsonRequest = objectMapper.writeValueAsString(request);

        Authentication authMock = mock(Authentication.class);
        User mockUser = new User(UUID.randomUUID(), "Denilson", "denilson@teste.com", "senha123");

        when(authMock.getPrincipal()).thenReturn(mockUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(tokenService.generateToken(mockUser)).thenReturn("eyJhbGciOiJIUzI1Ni... token falso");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1Ni... token falso"));
    }
}