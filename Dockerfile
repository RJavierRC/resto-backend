# =======  backend image ========
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/resto-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
