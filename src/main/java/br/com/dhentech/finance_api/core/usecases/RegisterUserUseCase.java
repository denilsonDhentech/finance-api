package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.UserRegistrationRequest;
import br.com.dhentech.finance_api.application.dto.UserResponse;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse execute(UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }
        String encryptedPassword = passwordEncoder.encode(request.password());

        User newUser = new User(null, request.name(), request.email(), encryptedPassword);

        User savedUser = userRepository.save(newUser);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }
}
