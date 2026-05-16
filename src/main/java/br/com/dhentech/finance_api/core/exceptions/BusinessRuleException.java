package br.com.dhentech.finance_api.core.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
