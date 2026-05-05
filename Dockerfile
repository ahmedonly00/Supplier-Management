# ── Build stage ──────────────────────────────────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM first — resolves dependencies as a separate cached layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build fat JAR (skip tests; tests run in CI)
COPY src src
RUN mvn package -DskipTests -B

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
