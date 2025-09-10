FROM eclipse-temurin:24-jre

WORKDIR /app

COPY party-archetype-spring/build/libs/party-archetype-spring.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

