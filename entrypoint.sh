#!/bin/sh

# Make sure target directory exists
mkdir -p /app/certs

# Copy the PEM files from mounted secrets
cp /etc/secrets/*.pem /app/certs/

# Optionally export as env vars if Spring Boot reads from env
export PRIVATE_KEY_PATH=/app/certs/private.pem
export PUBLIC_KEY_PATH=/app/certs/public.pem

# Start the Spring Boot application
exec java -jar /app/app.jar