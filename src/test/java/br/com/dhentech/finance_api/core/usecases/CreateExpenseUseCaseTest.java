package br.com.dhentech.finance_api.core.usecases;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.ports.ExpenseRepositoryPort;
import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.core.usecases.expenses.CreateExpenseUseCase;
import br.com.dhentech.finance_api.infrastructure.persistence.CategoryRepository;
import br.com.dhentech.finance_api.infrastructure.persistence.UserRepository;
import br.com.dhentech.finance_api.mapper.ExpenseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Mock
    private ExpenseMapper expenseMapper;

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
        UUID loggedUserId = dummyUser.getId();

        ExpenseRequest request = new ExpenseRequest("Academia", new BigDecimal("120.00"), LocalDate.now(), ExpenseType.RECURRING, fakeCategoryId);

        ExpenseResponse expectedResponse = new ExpenseResponse(
                UUID.randomUUID(), "Academia", new BigDecimal("120.00"),
                LocalDate.now(), ExpenseType.RECURRING, ExpenseStatus.PENDING, "Saúde"
        );

        when(categoryRepository.findById(fakeCategoryId)).thenReturn(Optional.of(dummyCategory));

        when(userRepository.findById(loggedUserId)).thenReturn(Optional.of(dummyUser));

        when(repositoryPort.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(expenseMapper.toResponse(any(Expense.class))).thenReturn(expectedResponse);

        // Act
        ExpenseResponse response = createExpenseUseCase.execute(request, loggedUserId);

        // Assert
        assertNotNull(response);
        assertEquals("Academia", response.description());

        verify(categoryRepository, times(1)).findById(fakeCategoryId);
        verify(userRepository, times(1)).findById(loggedUserId);
        verify(repositoryPort, times(1)).save(any(Expense.class));
        verify(expenseMapper, times(1)).toResponse(any(Expense.class));
    }
}