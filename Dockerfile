# base image
FROM openjdk:17-alpine


# copy files required for the app to run
ADD /target/crbservice-0.0.1-SNAPSHOT.jar conference-room-app.jar


# run the application
ENTRYPOINT ["java", "-jar", "/conference-room-app.jar"]


# tell the port number the container should expose
EXPOSE 8585