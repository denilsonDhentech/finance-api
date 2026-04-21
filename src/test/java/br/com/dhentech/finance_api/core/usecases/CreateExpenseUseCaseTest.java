package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateExpenseUseCaseTest {

    @Mock
    private ExpenseRepositoryPort repositoryPort;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateExpenseUseCase createExpenseUseCase;

    private User dummyUser;
    private Category dummyCategory;

    @BeforeEach
    void setUp() {
        dummyUser = new User(UUID.randomUUID(), "Dhen", "dhen@teste.com", "senha123");
        dummyCategory = new Category("Saúde", "Gastos com saúde e academia");
    }

    @Test
    void shouldCreateExpenseSuccessfully() {
        // Arrange
        UUID fakeCategoryId = UUID.randomUUID();

        ExpenseRequest request = new ExpenseRequest("Academia", new BigDecimal("120.00"), LocalDate.now(), ExpenseType.RECURRING, fakeCategoryId);

        when(categoryRepository.findById(fakeCategoryId)).thenReturn(Optional.of(dummyCategory));
        when(userRepository.findAll()).thenReturn(List.of(dummyUser));
        when(repositoryPort.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ExpenseResponse response = createExpenseUseCase.execute(request);

        // Assert
        assertNotNull(response.id());
        assertEquals("Academia", response.description());

        verify(categoryRepository, times(1)).findById(fakeCategoryId);
        verify(userRepository, times(1)).findAll();
        verify(repositoryPort, times(1)).save(any(Expense.class));
    }
}