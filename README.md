# Endpoints Monitoring Service

A service for monitoring HTTP/HTTPS URLs, allowing you to track their availability and receive information about status codes and responses.

## Features

-   Create, edit, delete, and view URLs for monitoring
-   Automatic background URL monitoring
-   Recording status codes and responses from URLs
-   Get the last 10 monitoring results for each URL

## Technologies

-   Java 11
-   Spring Boot 2.7
-   Spring MVC
-   Spring Data JPA
-   MySQL
-   JUnit 5
-   Docker

## Running the Application

### Running with Docker Compose (recommended)

1. Make sure you have Docker and Docker Compose installed
2. Clone the repository: `git clone https://github.com/krivopolnik/monitoringApp.git`
3. Navigate to the project directory: `cd monitoringApp`
4. Start the application: `docker-compose up -d`
5. The application will be available at: `http://localhost:8080`

### Running with Maven

1. Make sure you have Java 11 and MySQL installed
2. Clone the repository: `git clone https://github.com/krivopolnik/monitoringApp.git`
3. Navigate to the project directory: `cd monitoringApp`
4. Create a MySQL database named `monitoring_db`
5. Configure the database connection parameters in `src/main/resources/application.properties`
6. Build the project: `mvn clean package`
7. Run the application: `java -jar target/app-0.0.1-SNAPSHOT.jar`
8. The application will be available at: `http://localhost:8080`

## API Endpoints

### Authentication

Use the `X-Access-Token` header with the access token value for authentication.
The system has two predefined users:

1. **Applifting**

    - Email: info@applifting.cz
    - Access Token: 93f39e2f-80de-4033-99ee-249d92736a25

2. **Batman**
    - Email: batman@example.com
    - Access Token: dcb20f8a-5657-4f1b-9f7f-ce65739b359e

### URLs for Monitoring

-   `GET /api/endpoints` - Get all monitoring URLs for the current user
-   `GET /api/endpoints/{id}` - Get a monitoring URL by ID
-   `POST /api/endpoints` - Create a new monitoring URL
-   `PUT /api/endpoints/{id}` - Update a monitoring URL
-   `DELETE /api/endpoints/{id}` - Delete a monitoring URL
-   `GET /api/endpoints/{id}/results` - Get the last 10 monitoring results for a URL

### Health Check

-   `GET /api/health` - Check the service health

## Request Examples

### Creating a New Monitoring URL

```
POST /api/endpoints
X-Access-Token: 93f39e2f-80de-4033-99ee-249d92736a25
Content-Type: application/json

{
  "name": "Google",
  "url": "https://www.google.com",
  "monitoringInterval": 60
}
```

### Getting Monitoring Results

```
GET /api/endpoints/1/results
X-Access-Token: 93f39e2f-80de-4033-99ee-249d92736a25
```

## Project Structure

-   `src/main/java/com/monitoring/app/model` - Data models
-   `src/main/java/com/monitoring/app/dto` - DTOs for data transfer
-   `src/main/java/com/monitoring/app/repository` - Repositories for data access
-   `src/main/java/com/monitoring/app/service` - Business logic services
-   `src/main/java/com/monitoring/app/controller` - REST controllers
-   `src/main/java/com/monitoring/app/security` - Security components
-   `src/main/java/com/monitoring/app/exception` - Exception handling
-   `src/main/java/com/monitoring/app/config` - Configuration classes
-   `src/test` - Tests
