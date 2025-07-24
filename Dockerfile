


# Use a Java runtime as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/*.jar app.jar


# Document the port your app listens on (not strictly needed by Render, but good practice)
EXPOSE 8080

# Tell Docker how to run the app
CMD ["java", "-jar", "app.jar"]
