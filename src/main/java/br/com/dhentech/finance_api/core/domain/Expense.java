package br.com.dhentech.finance_api.core.domain;
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

    public Expense(String description, BigDecimal amount, LocalDate dueDate, ExpenseType type) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount must be positive");

        this.id = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.status = ExpenseStatus.PENDING;
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
}
