# Stage 1: Build the Spring Boot app using Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Set timezone to Asia/Manila
RUN ln -snf /usr/share/zoneinfo/Asia/Manila /etc/localtime && \
    echo "Asia/Manila" > /etc/timezone

COPY --from=builder /app/target/*.jar app.jar

#For render
# Copy the entrypoint script and make it executable
#COPY entrypoint.sh /entrypoint.sh
#RUN chmod +x /entrypoint.sh
#
## Create the target cert directory
#RUN mkdir -p /app/certs

# Expose the application port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

#For render
#ENTRYPOINT ["/entrypoint.sh"]