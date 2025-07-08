FROM amazoncorretto:21
COPY target/server-*.jar app.jar
ENTRYPOINT ["java", "-cp", "/app.jar", "ru.practicum.ShareItServer"]
