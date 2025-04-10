#!/bin/sh

# Load environment variables from .env file
set -a
source /app/.env
set +a

# Decode the PEM files from the environment variables and write them to disk
echo "$PRIVATE_PEM" | base64 -d > /app/certs/private.pem
echo "$PUBLIC_PEM" | base64 -d > /app/certs/public.pem

# Set the correct file permissions for the PEM files
chmod 600 /app/certs/*.pem

# Start the Spring Boot application
exec java -jar /app/app.jar
