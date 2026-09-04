FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S linkly && adduser -S linkly -G linkly
WORKDIR /app
COPY --from=build /app/target/linkly-api-*.jar app.jar
USER linkly
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
