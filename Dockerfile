FROM gradle:8.14.3-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ktor-htmx-all.jar /app/ktor-htmx-all.jar
ENTRYPOINT ["java","-jar","/app/ktor-htmx-all.jar"]
