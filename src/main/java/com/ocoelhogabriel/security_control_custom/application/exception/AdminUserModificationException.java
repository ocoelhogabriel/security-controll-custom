package com.ocoelhogabriel.security_control_custom.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando há uma tentativa de modificar um usuário administrador.
 * A anotação @ResponseStatus instrui o Spring a retornar um status HTTP 403 Forbidden
 * quando esta exceção não é tratada por um manipulador de exceções mais específico.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AdminUserModificationException extends RuntimeException {
    public AdminUserModificationException(String message) {
        super(message);
    }
}
