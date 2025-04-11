# Stage 1: Build the Spring Boot app using Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the whole project into the container
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# Stage 2: Use a minimal Java runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Set the timezone to Asia/Manila
RUN ln -snf /usr/share/zoneinfo/Asia/Manila /etc/localtime && \
    echo "Asia/Manila" > /etc/timezone

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Copy the entrypoint script and make it executable
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Create the target cert directory
RUN mkdir -p /app/certs

# Expose the application port
EXPOSE 8080

# Use the custom entrypoint script
ENTRYPOINT ["/entrypoint.sh"]