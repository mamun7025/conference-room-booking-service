# Conference Room Booking REST API service

Providing all available conference room for the current date, along with their capacities. User will be able to book a available room
for a time slot of 15 min interval.

Features:
- Optimal room search based on number of participants and capacity
- Configurable conference room operating hours (Ex: 8 hours, 10 hours..) and slot interval (Ex: 15min, 30min...) 
- Room maintenance time and overlap handling
- Concurrent booking request handling with database records locking



## Table of contents
* [Technology Stack](#technology-stack)
* [Project Structure](#project-structure)
* [Build and Run Project - 3 ways](#build-and-run-project---3-ways)
    + [Pull docker image from docker hub](#1-pull-docker-image-from-docker-hub)
    + [Build and run docker image locally](#2-build-and-run-docker-image-locally)
    + [By Intellij IDEA](#3-run-by-using-intellij-idea)
* [Business validation and Exception handling](#business-validation-and-exception-handling)
* [Database, Schema Design and ERD](#database-schema-design-and-erd)
* [Swagger / OpenAPI docs](#swagger--openapi-docs)
* [Postman collection](#postman-collection)
* [Special instruction and Test case](#special-instruction-and-test-case)
* [Author](#authors)

## Technology Stack
* Java 17
* Spring Boot
* Maven
* h2 Database - inMemory


## Project Structure
![](docs/project-structure.png)


## Build and Run Project - 3 ways

### 1. Pull docker image from docker hub
Pull and run docker image from docker hub
```
docker pull mn7025/conference-room-service
docker run -p 8585:8585 --name conference-room-service-cnt mn7025/conference-room-service
```

### 2. Build and run docker image locally
```
mvn clean install
docker build -t conference-room-service .
docker run -p 8585:8585 --name conference-room-service-cnt conference-room-service
```

### 3. Run by using Intellij IDEA
Clone project from github by following link.

```
https://github.com/mamun7025/conference-room-booking-service.git
```

Import project in Intellij IDEA and run.



## Business validation and Exception handling
### Business validation
Following business validation case is covered
* Minimum participant
* Overlap with maintenance time
* Optimal room search based on participants
* Current data
* Start time is less then end time



### Exception handling
* Handle all business exception with specific error code and message
* Example
>Request:
```
{
    "meetingTitle": "Retro Meeting",
    "numberOfParticipants": 3,
    "meetingStartTime": "2024-03-26 13:00",
    "meetingEndTime": "2024-03-26 13:30"
}
```
>Response with error code:
```
{
    "status": "error",
    "message": "Booking can not be done during maintenance time",
    "errorCode": "CR-0004"
}
```

## Database, Schema Design and ERD
* h2 in-memory database : to connect database use following properties

```
Browser URL: http://localhost:8585/crb-service/h2-console/
JDBC URL: jdbc:h2:file:./db/crb_dev_db;AUTO_SERVER=true
Username: sa
Password: root
```

* ERD Diagram
![](docs/ERD-Diagram.png)


* Tables with sample data
![](docs/schema-design-latest.png)

* Seed SQL scripts
>> Schema File: https://github.com/mamun7025/conference-room-booking-service/blob/main/src/main/resources/schema.sql
>> 
>> Data File: https://github.com/mamun7025/conference-room-booking-service/blob/main/src/main/resources/data.sql






## Swagger / OpenAPI docs
* Swagger UI
```
http://localhost:8585/crb-service/swagger-ui/index.html
```

* OpenAPI docs
```
http://localhost:8585/crb-service/v3/api-docs
```

* Snapshot
![](docs/Swagger-API.png)


* API Request and Response
-- add file here

## Postman collection


## Special instruction and Test case
### Special instruction
* Use fixed clock time during test case execution (fixed time: 2024-03-26 10:00)
* Postman collection is valid for a fix clock time (fixed time: 2024-03-26 10:00)
* Application clock time possible to control through configuration - only applicable for dev/testing env
* Change following application.yml properties to control clock time
```
app:
  time:
    zone-id: Asia/Dubai
    override-testing-time: true
    override-datetime-value: 2024-03-26 10:00
```

### Test case
* Cover test case more than 96%
* Some package excluded while running test cases which generally not require to test
* Add a picture for test coverage


## Authors
- [Al-Mamun ](https://github.com/mamun7025) > Java/Spring Boot/Microservice | Angular | RDBMS | FinTech
