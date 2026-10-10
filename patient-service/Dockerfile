# ============================================================
# STAGE 1: BUILD — Uses full JDK + Maven to compile the app
# ============================================================
# WHY eclipse-temurin? It's the most widely used, TCK-certified,
# production-grade OpenJDK distribution. Free for commercial use.
# WHY alpine? Minimal Linux (~5MB). Less surface area = fewer vulnerabilities.
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# TRICK: Copy only pom.xml first, then download dependencies.
# WHY? Docker caches each layer. If pom.xml hasn't changed,
# Docker reuses the cached dependencies layer — saving 2-5 minutes
# on every rebuild. Only when you change a dependency does this
# layer rebuild.
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Make the Maven wrapper executable and download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# NOW copy source code. This layer changes on every code edit,
# but the expensive dependency layer above is already cached.
COPY src src

# Build the JAR. Skip tests because:
# 1. Tests should run in CI BEFORE building the Docker image
# 2. Tests need a database — not available during image build
# 3. Keeps image build fast (~30s vs ~3min)
RUN ./mvnw package -DskipTests -B

# ============================================================
# STAGE 2: RUNTIME — Uses minimal JRE only (no JDK, no Maven)
# ============================================================
# WHY a separate stage? The build stage has Maven, source code,
# compiled classes, test files — none of that belongs in production.
# This stage copies ONLY the final JAR into a clean, minimal image.
FROM eclipse-temurin:21-jre-alpine AS runtime

# WHY a non-root user? Security best practice (OWASP).
# If an attacker exploits the app, they can't modify the OS
# or access host resources. Never run containers as root in production.
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

WORKDIR /app

# Copy ONLY the built JAR from the builder stage
# The --from=builder references Stage 1's filesystem
COPY --from=builder /app/target/*.jar app.jar

# Set ownership to the non-root user
RUN chown appuser:appgroup app.jar

USER appuser

# Document which port the app listens on.
# NOTE: EXPOSE doesn't publish the port — it's documentation.
# You still need -p 8080:8080 or docker-compose ports: mapping.
EXPOSE 8080

# Health check — Docker (and orchestrators like ECS/K8s) will
# ping this endpoint every 30s. If it fails 3 times, the
# container is marked unhealthy and can be restarted automatically.
# WHY /actuator/health? Spring Boot Actuator provides this out of the box.
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers:
# -XX:+UseContainerSupport  → JVM respects Docker memory limits (default in Java 21)
# -XX:MaxRAMPercentage=75   → Use at most 75% of container's RAM for heap
#                              (leave 25% for non-heap: metaspace, threads, GC)
# -Djava.security.egd       → Faster random number generation (important for SSL/sessions)
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/urandom", \
  "-jar", "app.jar"]
