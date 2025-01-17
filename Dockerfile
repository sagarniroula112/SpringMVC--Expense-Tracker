# Use a base Java image
FROM openjdk:21-jdk-slim

# Set working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/expensetracker-1.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]