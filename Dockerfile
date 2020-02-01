FROM openjdk:13
RUN mkdir /tmp/prevencaofraudes-svc
ADD . /tmp/prevencaofraudes-svc
RUN chmod +x /tmp/prevencaofraudes-svc/gradlew
WORKDIR /tmp/prevencaofraudes-svc
RUN ls -lsah
RUN ./gradlew clean build
RUN mv /tmp/prevencaofraudes-svc/build/libs/*.jar /tmp/app.jar
RUN rm -rf /tmp/prevencaofraudes-svc/

FROM adoptopenjdk:13.0.1_9-jre-openj9-0.17.0-bionic
COPY --from=0 /tmp/app.jar /tmp
RUN ls /tmp
ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]
EXPOSE 8081