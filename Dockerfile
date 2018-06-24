FROM openjdk:8-jdk-alpine

RUN mkdir -p /app
WORKDIR /app

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]