package br.com.dhentech.finance_api.infrastructure.persistence;

import br.com.dhentech.finance_api.core.domain.Category;
import br.com.dhentech.finance_api.core.domain.ExpenseStatus;
import br.com.dhentech.finance_api.core.domain.ExpenseType;
import br.com.dhentech.finance_api.core.domain.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_expenses")
public class ExpenseEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected ExpenseEntity() {}

    public ExpenseEntity(UUID id, String description, BigDecimal amount, LocalDate dueDate,
                         ExpenseType type, ExpenseStatus status, User user, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.status = status;
        this.user = user;
        this.category = category;
    }

    public UUID getId() { return id; }

    public String getDescription() { return description; }

    public BigDecimal getAmount() { return amount; }

    public LocalDate getDueDate() { return dueDate; }

    public ExpenseType getType() { return type; }

    public ExpenseStatus getStatus() { return status; }

    public User getUser() { return user; }

    public Category getCategory() { return category; }
}
