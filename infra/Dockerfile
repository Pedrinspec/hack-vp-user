# =============================================================================
# hack-vp-user — Dockerfile
# Spring Boot 3.3.5 + Java 21 + MySQL + JWT
# Multi-stage build: build → runtime
# =============================================================================

# -----------------------------------------------------------------------------
# Stage 1: Build
# -----------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copia apenas os arquivos de dependências primeiro (cache de layer otimizado)
COPY pom.xml ./

RUN mvn dependency:go-offline -B --no-transfer-progress

# Copia o código-fonte e compila
COPY src/ ./src/

RUN mvn package -DskipTests -B --no-transfer-progress

# -----------------------------------------------------------------------------
# Stage 2: Runtime — JRE slim
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL maintainer="FIAP Hackathon <fiap@fiap.com.br>"
LABEL app="hack-vp-user"
LABEL version="0.0.1-SNAPSHOT"

# Instala curl para healthcheck
RUN apk add --no-cache \
        curl \
    && rm -rf /var/cache/apk/*

# Usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia o JAR gerado no stage de build
COPY --from=builder /build/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

# Healthcheck usando a rota de API docs (sem actuator)
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -sf http://localhost:8080/v3/api-docs || exit 1

# Flags JVM otimizadas para container
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]