FROM openjdk:13

ADD build/libs/limite-svc-0.0.1-SNAPSHOT.jar /tmp

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/tmp/limite-svc-0.0.1-SNAPSHOT.jar"]
