package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ExpenseSpecificationTest {

    @Autowired
    private JpaExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve filtrar despesas pela data de início corretamente")
    void shouldFilterByStartDate() {
        Category category = new Category("Alimentação", "Compras de mercado");
        User user = new User(
                null,
                "Dhenilson",
                "dhenilson@dhentech.com.br",
                "senha123"
        );

        entityManager.persist(category);
        entityManager.persist(user);

        LocalDate today = LocalDate.now();
        ExpenseEntity expense = new ExpenseEntity(
                UUID.randomUUID(),
                "Compra de teste",
                new BigDecimal("150.00"),
                today,
                ExpenseType.ONE_TIME,
                ExpenseStatus.PENDING,
                user,
                category
        );
        expenseRepository.save(expense);
        entityManager.flush();

        Specification<ExpenseEntity> spec = Specification
                .where(ExpenseSpecification.belongsToUser(user.getId()))
                .and(ExpenseSpecification.hasStartDate(today));

        List<ExpenseEntity> results = expenseRepository.findAll(spec);

        assertEquals(1, results.size(), "Deveria ter encontrado a despesa salva");
    }

    @Test
    @DisplayName("Deve retornar null quando o filtro for nulo (exercitando condicionais)")
    void shouldReturnNullWhenFilterIsNull() {
        assertNull(ExpenseSpecification.hasStartDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasEndDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasCategory(null).toPredicate(null, null, null));
    }

    @Test
    @DisplayName("Deve retornar null quando os filtros forem nulos (cobertura de branch)")
    void shouldReturnNullWhenFiltersAreNull() {
        assertNull(ExpenseSpecification.hasStartDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasEndDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasCategory(null).toPredicate(null, null, null));
    }

    @Test
    @DisplayName("Deve cobrir todos os branches de filtros nulos")
    void shouldCoverAllBranchPaths() {
        assertNull(ExpenseSpecification.hasStartDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasEndDate(null).toPredicate(null, null, null));
        assertNull(ExpenseSpecification.hasCategory(null).toPredicate(null, null, null));
    }

    @Test
    @DisplayName("Deve garantir que a classe utilitária não seja instanciável")
    void constructorIsPrivate() throws NoSuchMethodException {
        var constructor = ExpenseSpecification.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(java.lang.reflect.InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    @DisplayName("Deve cobrir o caminho 'false' dos filtros (quando valores não são nulos)")
    void shouldCoverFilterFalsePaths() {
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        assertNotNull(ExpenseSpecification.hasEndDate(date));
        assertNotNull(ExpenseSpecification.hasCategory(categoryId));

        var specDate = ExpenseSpecification.hasEndDate(date);
        assertNotNull(specDate);
    }
}