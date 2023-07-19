FROM openjdk:17-jdk-slim-buster
WORKDIR /jumarket-autoatendimento-tqi
COPY build/libs/autoatendimento.system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]