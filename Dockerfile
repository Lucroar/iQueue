# Stage 1: Build the application using Maven + Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy all project files
COPY . .

# Build the Spring Boot application (skip tests if preferred)
RUN mvn clean package -DskipTests

# Stage 2: Use lightweight Java 21 runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Set timezone to Asia/Manila
RUN ln -snf /usr/share/zoneinfo/Asia/Manila /etc/localtime && \
    echo "Asia/Manila" > /etc/timezone

# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
