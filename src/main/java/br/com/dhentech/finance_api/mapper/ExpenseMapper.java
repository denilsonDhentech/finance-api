package br.com.dhentech.finance_api.mapper;

import br.com.dhentech.finance_api.application.dto.ExpenseResponse;
import br.com.dhentech.finance_api.core.domain.Expense;
import br.com.dhentech.finance_api.infrastructure.persistence.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(source = "category.name", target = "categoryName")
    ExpenseResponse toResponse(Expense expense);

    @Mapping(source = "category.name", target = "categoryName")
    ExpenseResponse toResponse(ExpenseEntity entity);

    ExpenseEntity toEntity(Expense expense);
}