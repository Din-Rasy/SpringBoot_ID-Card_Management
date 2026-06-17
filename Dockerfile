# Use official JDK 21 image
FROM eclipse-temurin:21-jdk

# Install dependencies: Maven, Git, NGINX, SSH, and PHP CLI
RUN apt-get update && apt-get install -y \
    maven \
    git \
    nginx \
    openssh-server \
    php-cli \
    && rm -rf /var/lib/apt/lists/*

# Configure SSH
RUN mkdir -p /var/run/sshd && \
    echo 'root:Hello@123' | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' /etc/pam.d/sshd

# Configure NGINX
RUN rm -f /etc/nginx/sites-enabled/default
COPY nginx.conf /etc/nginx/nginx.conf

# Setup working directory and copy project files
WORKDIR /app
COPY . /app

# Package the application
RUN mvn clean package -DskipTests

# Configure startup script
COPY start.sh /start.sh
RUN sed -i 's/\r$//' /start.sh && chmod +x /start.sh

# Expose ports
EXPOSE 8080 22 8081

# Set the entrypoint script
CMD ["/start.sh"]
