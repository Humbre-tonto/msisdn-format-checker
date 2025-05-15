# Use Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/msisdn-format-checker-0.0.1-SNAPSHOT.jar app.jar

# Expose the configured port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]