FROM openjdk:17-jdk-slim

LABEL authors="gojungsu"

ARG JAR_FILE=../tlog-was/build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]