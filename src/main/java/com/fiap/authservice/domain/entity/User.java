package com.fiap.authservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password; // Hash

    public void validate() {
        if (email == null || !email.contains("@")) {
            throw new DomainException("Email inválido");
        }
    }
}
