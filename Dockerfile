# Multi-stage Dockerfile for MenuApp (Spring Boot 4, Java 25)
# Build stage: use Eclipse Temurin JDK 25 to run the Maven wrapper and produce the fat jar
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Copy mvnw and .mvn so the wrapper works, then download dependencies (cacheable)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

# Copy source and build the application
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Runtime stage: use a lightweight Temurin JRE 25 image
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the jar produced in the build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose default Spring Boot port; change if your app uses another port
EXPOSE 8080

# Allow passing JVM options via the JAVA_OPTS environment variable
# Use sh -c form so the environment variable is expanded
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS:-} -jar /app/app.jar"]

