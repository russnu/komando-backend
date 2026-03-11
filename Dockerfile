FROM eclipse-temurin:17-jdk-jammy

LABEL authors="Russel"

WORKDIR /app

COPY komando-core/target/komandosb-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]