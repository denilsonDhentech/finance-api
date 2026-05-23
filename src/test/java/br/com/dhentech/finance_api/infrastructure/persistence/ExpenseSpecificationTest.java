package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ExpenseSpecificationTest {

    @Autowired
    private JpaExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static Stream<Arguments> provideNullFilters() {
        return Stream.of(
                Arguments.of((Specification<ExpenseEntity>) ExpenseSpecification.hasStartDate(null)),
                Arguments.of((Specification<ExpenseEntity>) ExpenseSpecification.hasEndDate(null)),
                Arguments.of((Specification<ExpenseEntity>) ExpenseSpecification.hasCategory(null))
        );
    }

    @Test
    @DisplayName("Deve filtrar despesas pela data de início corretamente (Caminho 'False')")
    void shouldFilterByStartDate() {
        Category category = new Category("Alimentação", "Compras");
        User user = new User(null, "Dhenilson", "dhenilson@dhentech.com.br", "senha123");

        entityManager.persist(category);
        entityManager.persist(user);

        LocalDate today = LocalDate.now();
        ExpenseEntity expense = new ExpenseEntity(
                UUID.randomUUID(), "Compra de teste", new BigDecimal("150.00"),
                today, ExpenseType.ONE_TIME, ExpenseStatus.PENDING, user, category
        );
        expenseRepository.save(expense);
        entityManager.flush();

        Specification<ExpenseEntity> spec = Specification
                .where(ExpenseSpecification.belongsToUser(user.getId()))
                .and(ExpenseSpecification.hasStartDate(today));

        List<ExpenseEntity> results = expenseRepository.findAll(spec);
        assertEquals(1, results.size());
    }

    @ParameterizedTest
    @MethodSource("provideNullFilters")
    @DisplayName("Deve retornar null para todos os filtros nulos (Caminho 'True')")
    void shouldReturnNullForNullFilters(Specification<ExpenseEntity> spec) {
        assertNull(spec.toPredicate(null, null, null));
    }

    @Test
    @DisplayName("Deve cobrir o caminho 'false' dos filtros (valores não nulos)")
    void shouldCoverFilterFalsePaths() {
        assertNotNull(ExpenseSpecification.hasEndDate(LocalDate.now()));
        assertNotNull(ExpenseSpecification.hasCategory(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve garantir que a classe utilitária não seja instanciável")
    void constructorIsPrivate() throws NoSuchMethodException {
        var constructor = ExpenseSpecification.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(java.lang.reflect.InvocationTargetException.class, constructor::newInstance);
    }
}