# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

RUN useradd -ms /bin/bash dev

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline   # Downloads all dependencies (cached)

COPY src ./src
RUN mvn clean package -DskipTests

RUN chown -R dev:dev /app

USER dev

RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

USER 1000:1000

EXPOSE 8079

ENTRYPOINT ["java", "-jar", "app.jar"]
