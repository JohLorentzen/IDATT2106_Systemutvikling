FROM maven:3.9.6 AS build

WORKDIR /app/backend

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM build
COPY --from=build /app/backend/target/sparesti-1.0-SNAPSHOT.jar sparesti-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sparesti-backend.jar"]