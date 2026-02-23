FROM maven:3.9.12-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY package.json package-lock.json* ./

RUN mvn -B dependency:go-offline

COPY src ./src
COPY vite.config.* ./

RUN mvn -B clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", "-jar", "app.jar"]