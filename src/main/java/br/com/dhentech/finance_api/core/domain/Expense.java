package br.com.dhentech.finance_api.core.domain;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Expense {

    private UUID id;
    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private ExpenseType type;
    private ExpenseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Expense(){}

    public Expense(String description, BigDecimal amount, LocalDate dueDate, ExpenseType type, User user, Category category) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount must be positive");

        this.id = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.status = ExpenseStatus.PENDING;
        this.user = user;
        this.category = category;
    }

    public void markAsPaid() {
        if (this.status == ExpenseStatus.PAID) {
            throw new IllegalStateException("This expense is already paid.");
        }
        this.status = ExpenseStatus.PAID;
    }


    public UUID getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
    public ExpenseType getType() { return type; }
    public ExpenseStatus getStatus() { return status; }
    public Category getCategory() { return category; }
    public User getUser() { return this.user; }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
