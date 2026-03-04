package com.fiap.authservice.auth.application.port.out;

import com.fiap.authservice.auth.domain.model.User;

public interface TokenGeneratorPort {

    String generate(User user);
}
