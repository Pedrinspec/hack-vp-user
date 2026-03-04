package com.fiap.authservice.auth.application.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Usuário já existe");
    }
}
