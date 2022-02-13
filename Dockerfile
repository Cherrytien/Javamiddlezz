FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/Javamiddle.jar Javamiddle.jar
ENTRYPOINT ["java","-jar","/Javamiddle.jar", "&"]