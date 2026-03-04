package com.fiap.authservice.auth.infrastructure.security;

import com.fiap.authservice.auth.application.port.out.TokenGeneratorPort;
import com.fiap.authservice.auth.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenService implements TokenGeneratorPort {

    private final JwtProperties properties;

    public JwtTokenService(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(properties.expirationSeconds());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail().value())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey())
                .compact();
    }

    public UUID extractSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return UUID.fromString(claims.getSubject());
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
