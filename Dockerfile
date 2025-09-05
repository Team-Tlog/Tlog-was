FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY . /app

WORKDIR /app/tlog-was
RUN ./gradlew build -x test


FROM openjdk:17-jdk-slim

LABEL authors="gojungsu"

COPY --from=builder /app/tlog-was/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app.jar"]