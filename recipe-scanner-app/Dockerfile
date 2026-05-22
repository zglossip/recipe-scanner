FROM gradle:9-jdk25 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
ARG GITHUB_ACTOR
ARG GITHUB_TOKEN
RUN gradle :recipe-scanner-app:bootJar --no-daemon

FROM eclipse-temurin:25-jre
RUN apt-get update && \
    apt-get install -y --no-install-recommends tesseract-ocr tesseract-ocr-eng && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/recipe-scanner-app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]