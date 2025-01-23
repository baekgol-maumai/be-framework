FROM azul/zulu-openjdk:21-jre
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]