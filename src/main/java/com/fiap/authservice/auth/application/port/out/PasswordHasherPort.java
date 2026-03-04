package com.fiap.authservice.auth.application.port.out;

public interface PasswordHasherPort {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}
