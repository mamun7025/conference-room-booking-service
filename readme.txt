Conference Room Booking Backend service


>>>> Build and Run Locally
mvn clean install
docker build -t conference-room-service .
docker run -p 8585:8585 --name conference-room-service-cnt conference-room-service


>>>> Pull Image and Run Locally
docker pull mn7025/conference-room-service
docker run -p 8585:8585 --name conference-room-service-cnt mn7025/conference-room-service

