# 1) Build stage
FROM gradle:8.10.2-jdk17 AS build

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

# Disabilito i test durante la build
RUN gradle clean bootJar -x test

# 2) Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copio il jar generato
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Espongo la porta Spring Boot (di default 8080)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
