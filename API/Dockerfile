FROM maven:latest AS build

WORKDIR /app

COPY . .

RUN mv src/main/resources/application.properties.example src/main/resources/application.properties

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

RUN apt-get update && apt-get install -y python3 python3-pip && apt-get clean

RUN pip3 install rdflib

RUN mkdir Logs

COPY --from=build /app/target/pir-rdf-0.0.1-SNAPSHOT.jar .
COPY --from=build /app/json2rdf.py .

CMD ["java", "-jar", "pir-rdf-0.0.1-SNAPSHOT.jar"]
