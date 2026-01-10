# -----------------------------------------------------------
# Multi-stage Dockerfile for Kaleo Spring Boot Application
# -----------------------------------------------------------

# Stage 1: Build the application
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first for better layer caching
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies (cached layer unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests -B

# -----------------------------------------------------------
# Stage 2: Runtime image
# -----------------------------------------------------------
FROM eclipse-temurin:25-jre

WORKDIR /app

# Create a non-root user for security
RUN groupadd -r kaleo && useradd -r -g kaleo kaleo

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown kaleo:kaleo app.jar

# Switch to non-root user
USER kaleo

# Expose the application port
EXPOSE 8080

# Run the application with optimized JVM settings
ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:+ZGenerational", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
