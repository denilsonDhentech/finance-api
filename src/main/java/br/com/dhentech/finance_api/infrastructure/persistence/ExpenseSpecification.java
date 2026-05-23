package br.com.dhentech.finance_api.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ExpenseSpecification {

    private ExpenseSpecification() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    public static Specification<ExpenseEntity> belongsToUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ExpenseEntity> hasStartDate(LocalDate startDate) {
        return (root, query, cb) -> startDate == null ? null :
                cb.greaterThanOrEqualTo(root.get("dueDate"), startDate);
    }

    public static Specification<ExpenseEntity> hasEndDate(LocalDate endDate) {
        return (root, query, cb) -> endDate == null ? null :
                cb.lessThanOrEqualTo(root.get("dueDate"), endDate);
    }

    public static Specification<ExpenseEntity> hasCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null :
                cb.equal(root.get("category").get("id"), categoryId);
    }
}
