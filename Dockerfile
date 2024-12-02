# Step 1: Build the application
FROM gradle:8.5-jdk17-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle configuration files
COPY build.gradle settings.gradle ./

# Copy Gradle wrapper files
COPY gradlew ./
COPY gradle/ ./gradle/

# Pre-fetch dependencies to speed up future builds
RUN ./gradlew --no-daemon dependencies

# Copy the rest of the application source code
COPY . ./

# Build the Spring Boot JAR file
RUN ./gradlew bootJar --no-daemon --parallel

# Step 2: Production stage with minimal base image
FROM eclipse-temurin:23-jre-alpine AS production

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar concordia-backend.jar

# (Optional) Copy resources if needed by the application
# Uncomment this line if your app needs direct access to specific YAML/other files
# COPY --from=builder /app/src/main/resources/*.yml ./resources/

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "concordia-backend.jar"]
