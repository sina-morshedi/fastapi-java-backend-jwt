# Use an official Maven image with Java 17 to build the project
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cache)
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy all source code
COPY src ./src

# Build the project (package as jar)
RUN mvn clean package -DskipTests

# Use a lightweight JRE 17 image to run the app
FROM eclipse-temurin:17

RUN apt-get update && apt-get install -y ca-certificates

java -Djdk.tls.client.protocols=TLSv1.2 -jar app.jar \


WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar"]

