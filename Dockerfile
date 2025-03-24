FROM eclipse-temurin:23-jdk AS spring-builder

WORKDIR /code_folder

COPY light-touch/pom.xml light-touch/mvnw /code_folder/
COPY light-touch/.mvn /code_folder/.mvn

COPY light-touch/src /code_folder/src

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jdk

COPY --from=spring-builder /code_folder/target/light-touch-0.0.1-SNAPSHOT.jar app.jar

# Expose the server port as an environment variable
ENV SERVER_PORT=1471

# Expose the port for external access
EXPOSE ${SERVER_PORT}

# Entrypoint to run the final applicatiom
ENTRYPOINT ["java", "-jar",  "app.jar"]
CMD ["--server.port=1471"]