# Use a base Java image
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN mvn clean package

# Use a lightweight Java runtime for the final image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/expensetracker-1.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
