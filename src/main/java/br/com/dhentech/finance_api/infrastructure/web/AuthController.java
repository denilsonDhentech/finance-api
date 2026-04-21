package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.AuthenticationRequest;
import br.com.dhentech.finance_api.application.dto.TokenResponse;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            var usernamePasswordToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication auth = authenticationManager.authenticate(usernamePasswordToken);
            String token = tokenService.generateToken((User) auth.getPrincipal());

            log.info("🔐 Login realizado com sucesso para: {}", request.email());

            return ResponseEntity.ok(new TokenResponse(token));

        } catch (Exception e) {
            log.warn("❌ Falha de autenticação para {}: {}", request.email(), e.getMessage());
            throw e;
        }
    }
}