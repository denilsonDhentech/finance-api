package br.com.dhentech.finance_api.infrastructure.web;

import br.com.dhentech.finance_api.application.dto.ExpenseRequest;
import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.User;
import br.com.dhentech.finance_api.core.usecases.CreateExpenseUseCase;
import br.com.dhentech.finance_api.core.usecases.GetExpenseByIdUseCase;
import br.com.dhentech.finance_api.core.usecases.ListExpensesUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    public ExpenseController(CreateExpenseUseCase createExpenseUseCase,
                             ListExpensesUseCase listExpensesUseCase,
                             GetExpenseByIdUseCase getExpenseByIdUseCase) {
        this.createExpenseUseCase = createExpenseUseCase;
        this.listExpensesUseCase = listExpensesUseCase;
        this.getExpenseByIdUseCase = getExpenseByIdUseCase;
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
    public ResponseEntity<Page<ExpenseResponse>> list(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 10, sort = "dueDate") Pageable pageable
    ) {
        Page<ExpenseResponse> response = listExpensesUseCase.execute(loggedUser.getId(), pageable);
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
}
