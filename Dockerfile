FROM openjdk:13
RUN mkdir /tmp/limite-svc
ADD . /tmp/limite-svc
RUN chmod +x /tmp/limite-svc/gradlew
WORKDIR /tmp/limite-svc
RUN ls -lsah
RUN ./gradlew clean build
RUN mv /tmp/limite-svc/build/libs/*.jar /tmp/app.jar
RUN rm -rf /tmp/limite-svc/
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]