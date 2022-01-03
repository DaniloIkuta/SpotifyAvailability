FROM maven:3.8.4-openjdk-17@sha256:b52dfe7b058e3db1cd677a0b1d0fa618f3b9e891d7cd1549f3c3d1c12aa6acbd AS build
RUN mkdir /app
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17.0.1_12-jre-alpine@sha256:2789cbf2fbb635ec417ab4cb60ad8a03289876e19c8586502eaf0bb22cf25298
RUN apk add dumb-init
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /app/target/spotify-availability-0.0.1-SNAPSHOT.jar /app
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8080
CMD "dumb-init" "java" "-jar" "spotify-availability-0.0.1-SNAPSHOT.jar"


#ARG APP_VERSION
#VOLUME /tmp
#COPY /target/*.jar /app/spotify-availability-${APP_VERSION}.jar
#ENTRYPOINT ["java","-jar","/spotify-availability-${APP_VERSION}.jar"]