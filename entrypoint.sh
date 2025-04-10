#!/bin/sh

# Load the environment variables from the .env file located in /etc/secrets/
set -a
source /etc/secrets/.env
set +a

# Decode the PEM files from the environment variables and save them in the appropriate location
echo "$PRIVATE_PEM" | base64 -d > /app/certs/private.pem
echo "$PUBLIC_PEM" | base64 -d > /app/certs/public.pem

# Set the correct permissions for the PEM files
chmod 600 /app/certs/*.pem

# Start the Spring Boot application
exec java -jar /app/app.jar