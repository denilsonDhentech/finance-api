package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.ExpenseFilter;
import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.application.dto.PagedResponse;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.core.usecases.expenses.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final CreateExpenseUseCase createExpenseUseCase;
    private final ListExpensesUseCase listExpensesUseCase;
    private final GetExpenseByIdUseCase getExpenseByIdUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;

    public ExpenseController(CreateExpenseUseCase createExpenseUseCase,
                             ListExpensesUseCase listExpensesUseCase,
                             GetExpenseByIdUseCase getExpenseByIdUseCase,
                             UpdateExpenseUseCase updateExpenseUseCase,
                             DeleteExpenseUseCase deleteExpenseUseCase) {
        this.createExpenseUseCase = createExpenseUseCase;
        this.listExpensesUseCase = listExpensesUseCase;
        this.getExpenseByIdUseCase = getExpenseByIdUseCase;
        this.updateExpenseUseCase = updateExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(
            @RequestBody @Valid ExpenseRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ExpenseResponse response = createExpenseUseCase.execute(request, loggedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ExpenseResponse>> list(
            @AuthenticationPrincipal User loggedUser,
            ExpenseFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<ExpenseResponse> response = listExpensesUseCase.execute(
                filter,
                page,
                size,
                loggedUser.getId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User loggedUser
    ) {
        ExpenseResponse response = getExpenseByIdUseCase.execute(id, loggedUser.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid ExpenseRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ExpenseResponse response = updateExpenseUseCase.execute(id, request, loggedUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User loggedUser
    ) {
        deleteExpenseUseCase.execute(id, loggedUser.getId());
        return ResponseEntity.noContent().build();
    }
}
