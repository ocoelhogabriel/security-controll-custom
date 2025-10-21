package com.ocoelhogabriel.security_control_custom.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma Abrangência (Scope) não é encontrada.
 * Mapeia para o status HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScopeNotFoundException extends RuntimeException {
    public ScopeNotFoundException(String message) {
        super(message);
    }
}
