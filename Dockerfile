# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Copy the Spring Boot jar file
COPY build/libs/customerapp-0.0.1-customerapp.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]