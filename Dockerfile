FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/java-assignment-3.jar java-assignment-3.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "java-assignment-3.jar"]
