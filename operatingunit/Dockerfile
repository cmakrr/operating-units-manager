FROM maven:3.9.9-eclipse-temurin-17 AS build
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY pom.xml $APP_HOME
COPY src $APP_HOME/src/
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-oracle
ENV APP_HOME=/app/
ENV ARTIFACT_NAME=operatingunit-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=$APP_HOME/target/$ARTIFACT_NAME
COPY --from=build $JAR_FILE /opt/operatingunit/app.jar
ENTRYPOINT ["java", "-jar", "/opt/operatingunit/app.jar"]