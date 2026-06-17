#!/bin/bash

# Start SSH daemon
echo "Starting SSH daemon..."
/usr/sbin/sshd

# Start NGINX
echo "Starting NGINX..."
nginx

# Wait for MySQL database container to be ready
echo "Waiting for MySQL database connection..."
until bash -c 'exec 6<>/dev/tcp/mysql/3306' 2>/dev/null; do
  echo "MySQL is not ready yet. Retrying in 2 seconds..."
  sleep 2
done

echo "MySQL is ready! Starting Spring Boot..."
# Execute Spring Boot application
exec java -jar /app/target/springboot-id-card-management-0.0.1-SNAPSHOT.jar
