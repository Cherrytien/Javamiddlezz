FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/javamiddle.jar javamiddle.jar
ENTRYPOINT ["java","-jar","/javamiddle.jar", "&"]