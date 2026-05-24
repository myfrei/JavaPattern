# ── build stage ──────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# cache dependencies first
COPY pom.xml .
RUN mvn -B dependency:go-offline

# build the executable jar
COPY src ./src
RUN mvn -B clean package -DskipTests

# ── runtime stage ────────────────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
