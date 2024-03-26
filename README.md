# Conference Room Booking REST API service

will show all available conference room in current date with capacity and user will be able to book available room
for a definite 15 min time interval slot.
with slot information.

Features:
- Optimal room search based on number of participants and capacity
- Room maintenance time and overlap handling
- Concurrent request handling



## Table of contents
* [Technology Stack](#technology-stack)
* [Project Structure](#technologies)
* [Install and Run Project - 3 ways](#technologies)
    + [Run in Docker](#install-python)
    + [Command Line](#install-virtualenv)
    + [Intellij IDEA](#install-virtualenv)
* [Database & Schema Design](#install-python)
* [Swagger and API Docs](#install-python)
* [Postman Collection](#run-backend-application)
* [Test Case](#run-backend-application)
* [Author](#enviorment-setup---frontend)

## Technology Stack
* Java 17
* Spring Boot
* Maven
* h2 Database


## Project Structure

![](docs/project-structure.png)

## Install and Run Project - 3 ways

## Database & Schema Design

![](docs/schema-design.png)

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