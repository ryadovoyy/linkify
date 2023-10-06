FROM gradle:8.3-jdk17-alpine AS build
WORKDIR /source
COPY build.gradle .
COPY settings.gradle .
COPY src src/
RUN --mount=type=cache,target=/root/.gradle \
    gradle --build-cache --parallel --no-daemon bootJar

FROM eclipse-temurin:17-jre-alpine
USER guest
WORKDIR /app
COPY --chown=guest --from=build /source/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
