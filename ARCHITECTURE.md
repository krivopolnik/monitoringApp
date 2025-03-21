# Application Architecture

## Overview

The application is implemented as a REST API microservice in Java using Spring Boot. The service allows users to create, edit, and delete endpoints for monitoring, as well as view monitoring results.

## Application Layers

### Data Model (model)

-   `User` - system user with access token
-   `MonitoredEndpoint` - endpoint to be monitored
-   `MonitoringResult` - endpoint monitoring result

### Data Access Layer (repository)

-   Spring Data JPA is used for working with MySQL database
-   Repositories provide methods for data access

### Service Layer (service)

-   `UserService` - service for working with users, initializes predefined users
-   `MonitoredEndpointService` - service for CRUD operations with endpoints
-   `MonitoringResultService` - service for working with monitoring results
-   `MonitoringService` - background service that checks endpoints at specified intervals

### Controllers (controller)

-   `MonitoredEndpointController` - handles requests for CRUD operations with endpoints
-   `HealthController` - service health check

### Security (security)

-   Authentication via `X-Access-Token` header
-   `AuthenticationFilter` checks the token and extracts the user

### DTO (Data Transfer Objects)

-   `MonitoredEndpointDto` - for transferring endpoint data
-   `MonitoringResultDto` - for transferring monitoring results
-   `ErrorResponse` - for sending error information

### Exception Handling (exception)

-   `GlobalExceptionHandler` - global exception handler
-   `ResourceNotFoundException` - exception for missing resources

## Background Monitoring Operation

1. `MonitoringService` runs every second (via `@Scheduled` annotation)
2. For each endpoint, it checks if enough time has passed since the last check
3. If it's time to check, the service sends an HTTP request to the endpoint URL
4. The check result (status code, response body) is saved in the database
5. The endpoint's last check date is updated

## Authorization and Authentication

1. The user makes a request with the `X-Access-Token` header
2. `AuthenticationFilter` checks the token and finds the corresponding user
3. The user is attached to the request as an attribute
4. Controllers get the user from the request
5. Authorization check: a user can only see and edit their own endpoints

## Running in Docker

-   `Dockerfile` defines two stages:
    -   Building the project with Maven
    -   Running the JAR file on JRE
-   `docker-compose.yml` defines two services:
    -   Java application
    -   MySQL database
