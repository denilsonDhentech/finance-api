package br.com.dhentech.finance_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
}
