# syntax=docker/dockerfile:1.7

# === Stage 1: Build ===
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy pom.xml and download dependencies (cached separately)
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -B dependency:go-offline

# Copy source and build the JAR
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests package \
    && cp target/*.jar /workspace/app.jar

# === Stage 2: Run ===
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]

