# First stage: build the Spring Boot app using Maven
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
LABEL authors="loh_J"

WORKDIR /equiz-platform-backend

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src/ ./src/

# Build the Spring Boot app (this will generate the JAR file)
RUN mvn clean package -DskipTests

# Second stage: run the app in a OpenJDK container
FROM eclipse-temurin:21-alpine

WORKDIR /equiz-platform-backend

# Copy JAR file from build stage
COPY --from-builder /equiz-platform-backend/target/EQuizPlatform-0.0.1-SNAPSHOT.jar EQuizPlatform.jar

EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/equiz-platform-backend/EQuizPlatform.jar"]