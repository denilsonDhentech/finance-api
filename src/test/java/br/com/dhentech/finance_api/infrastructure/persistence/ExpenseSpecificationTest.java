package br.com.dhentech.finance_api.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ExpenseSpecificationTest {

    @Autowired
    private JpaExpenseRepository expenseRepository;

    @Test
    @DisplayName("Deve filtrar despesas pela data de início corretamente")
    void shouldFilterByStartDate() {

        UUID userId = UUID.randomUUID();
        LocalDate filterDate = LocalDate.now();

        Specification<ExpenseEntity> spec = Specification
                .where(ExpenseSpecification.belongsToUser(userId))
                .and(ExpenseSpecification.hasStartDate(filterDate));

        List<ExpenseEntity> results = expenseRepository.findAll(spec);

        assertEquals(0, results.size());
    }
}