# auth-service

Projeto de autenticação com **Spring Boot 3.x** e **Java 21**, organizado em camadas:

- `com.example.authservice.auth.domain`: entidades e VOs sem dependência de Spring/JPA.
- `com.example.authservice.auth.application`: casos de uso e portas (entrada/saída).
- `com.example.authservice.auth.infrastructure`: adapters concretos (JPA, controllers REST, JWT, configurações).

## Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`

## Executar

```bash
mvn spring-boot:run
```
