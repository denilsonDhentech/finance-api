package br.com.dhentech.finance_api.infrastructure.security;

import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username);

        if (user == null) {
            log.warn("⚠️ Tentativa de login com e-mail inexistente: {}", username);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return user;
    }
}