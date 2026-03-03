# auth-service

Serviço de autenticação para emissão de token JWT, pensado para ambiente de microserviços onde outros serviços (ex.: upload e processamento de vídeos) delegam autenticação/autorização para este componente.

## Visão da arquitetura

O projeto segue uma organização em camadas (estilo Clean Architecture), separando regras de negócio, casos de uso e integrações externas.

### Camadas e responsabilidades

- **Domain (`auth.domain`)**
  - Entidades e Value Objects.
  - Regras de negócio puras, sem dependência de framework.
- **Application (`auth.application`)**
  - Casos de uso (`RegisterUserService`, `LoginService`).
  - Portas de entrada/saída (`port.in`, `port.out`) para desacoplar regras de negócio de tecnologia.
  - Exceções de negócio (`UserAlreadyExistsException`, `InvalidCredentialsException`).
- **Infrastructure (`auth.infrastructure`)**
  - Controllers REST (`AuthController`) e DTOs HTTP.
  - Implementações concretas das portas (JPA, hash de senha com bcrypt, geração de JWT).
  - Configurações técnicas (beans, segurança, handlers de erro).

### Fluxo de login

1. Cliente chama `POST /api/auth/login` com `email` e `password`.
2. `AuthController` valida o payload e converte para `LoginCommand`.
3. `LoginService` busca o usuário via `UserRepositoryPort`.
4. Senha informada é validada via `PasswordHasherPort` (bcrypt).
5. Em caso de sucesso, o token é emitido por `TokenGeneratorPort` (JWT).
6. API retorna `200 OK` com `{ "accessToken": "..." }`.
7. Em falha de autenticação, retorna `401 Unauthorized`.

## Execução local

### Pré-requisitos

- Java 21
- Maven 3.9+
- Banco PostgreSQL (recomendado para `UUID` + `TIMESTAMP WITH TIME ZONE` das migrations Flyway)

### Subir aplicação

```bash
mvn clean spring-boot:run
```

### Build

```bash
mvn clean package
```

### Testes

```bash
mvn test
```

> Configure as variáveis de ambiente/propriedades da aplicação para conexão com banco e parâmetros JWT antes de executar em ambiente local.

## Contratos da API

Base path: `/api/auth`

### `POST /api/auth/register`

Cria um novo usuário e retorna token de acesso.

**Request**

```json
{
  "email": "user@example.com",
  "password": "SenhaForte123"
}
```

Validações:
- `email`: obrigatório e formato de e-mail válido.
- `password`: obrigatório, mínimo 8 e máximo 100 caracteres.

**Responses**

- `201 Created`

```json
{
  "accessToken": "<jwt>"
}
```

- `409 Conflict` (usuário já existe)

```json
{
  "error": "<mensagem de negócio>"
}
```

- `400 Bad Request` (payload inválido)

```json
{
  "error": "Invalid request payload"
}
```

---

### `POST /api/auth/login`

Autentica usuário e retorna token de acesso.

**Request**

```json
{
  "email": "user@example.com",
  "password": "SenhaForte123"
}
```

Validações:
- `email`: obrigatório e formato de e-mail válido.
- `password`: obrigatório.

**Responses**

- `200 OK`

```json
{
  "accessToken": "<jwt>"
}
```

- `401 Unauthorized` (credenciais inválidas)

```json
{
  "error": "<mensagem de negócio>"
}
```

- `400 Bad Request` (payload inválido)

```json
{
  "error": "Invalid request payload"
}
```

## Migrations (Flyway)

As migrations ficam em:

- `src/main/resources/db/migration`

Script inicial:

- `V1__create_users_table.sql`
  - Cria tabela `users`.
  - Define `PRIMARY KEY` em `id`.
  - Define `UNIQUE` para `email`.
  - Cria índices para `email` e `created_at`.

## Decisões arquiteturais (contexto de microserviços)

- **Serviço de autenticação exclusivo**
  - Centraliza identidade e credenciais em um único ponto.
  - Evita duplicação de lógica de login/JWT em serviços de domínio (ex.: vídeos, upload, processamento).
- **Separação entre autenticação e domínio de vídeo**
  - Serviços de vídeo focam em regras de negócio (upload, transcodificação, catálogo).
  - Autorização entre serviços pode usar token JWT assinado por este auth-service.
- **Evolução independente**
  - Permite escalar autenticação de forma isolada.
  - Facilita mudanças futuras (MFA, OAuth2/OIDC, rotação de chaves, refresh tokens) sem acoplar outros serviços.
- **Contratos HTTP claros**
  - Endpoints e códigos de erro padronizados simplificam integração entre gateway/BFF e serviços consumidores.
