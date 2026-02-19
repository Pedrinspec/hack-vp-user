package com.example.authservice.auth.application.port.out;

import java.util.Map;

public interface TokenGeneratorPort {
    String generate(String subject, Map<String, Object> claims);
}
