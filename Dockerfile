FROM gradle:7-jdk11 AS build
RUN XDG_CACHE_HOME="$HOME/.gradle:$XDG_CACHE_HOME"
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ktor-htmx-all.jar /app/ktor-htmx-all.jar
ENTRYPOINT ["java","-jar","/app/ktor-htmx-all.jar"]