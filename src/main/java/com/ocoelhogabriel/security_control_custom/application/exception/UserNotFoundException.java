package com.ocoelhogabriel.security_control_custom.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário não é encontrado.
 * A anotação @ResponseStatus instrui o Spring a retornar um status HTTP 404 Not Found
 * quando esta exceção não é tratada por um manipulador de exceções mais específico.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
