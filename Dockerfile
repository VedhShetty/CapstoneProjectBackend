# Use Java 17 (matches your project)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built JAR into the container
COPY target/BankingAccountManagement-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
