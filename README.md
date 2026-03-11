# auth-service

Projeto de autenticação com **Spring Boot 3.x** e **Java 21**, organizado em camadas:

- `com.fiap.authservice.domain`: entidades e portas do domínio sem dependência de Spring.
- `com.fiap.authservice.application`: casos de uso e DTOs da aplicação.
- `com.fiap.authservice.infrastructure`: adapters concretos (JPA, controllers REST, JWT, configurações).

## Endpoints

- `POST /auth/register`
- `POST /auth/login`

## Swagger / OpenAPI

Após subir a aplicação, a documentação fica disponível em:

- UI: `http://localhost:8080/swagger-ui/index.html`
- JSON: `http://localhost:8080/v3/api-docs`

## Executar

```bash
mvn spring-boot:run
```
