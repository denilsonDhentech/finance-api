package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.core.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
    Page<ExpenseEntity> findAllByUser_IdOrderByDueDateDesc(UUID loggedUserId, Pageable page);
    Optional<ExpenseEntity> findByIdAndUser_Id(UUID id, UUID userId);
}
