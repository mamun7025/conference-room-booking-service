Conference Room Booking Backend service

Run application | 3 ways
>>>> Build and Run locally
mvn clean install
docker build -t conference-room-service .
docker run -p 8585:8585 --name conference-room-service-cnt conference-room-service


>>>> Push image to docker hub
docker images
docker tag conference-room-service:latest mn7025/conference-room-service:latest
docker push mn7025/conference-room-service:latest


>>>> Pull image and run
docker pull mn7025/conference-room-service
docker image ls
docker run -p 8585:8585 --name conference-room-service-cnt conference-room-service 			>>>> Own account
docker run -p 8585:8585 --name conference-room-service-cnt mn7025/conference-room-service   >>>> Other PC account