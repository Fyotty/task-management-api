'''
# Task Management API

[![Java 17](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) [![Spring Boot 3.1.5](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Maven](https://img.shields.io/badge/Maven-3.9.4-orange.svg)](https://maven.apache.org/) [![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

RESTful API for task and subtask management, built with Spring Boot, providing a complete solution for creating, tracking, and managing tasks and their associated subtasks.

This project was completely refactored to incorporate best practices such as:
-   **English-first:** All code, including entities, DTOs, services, and controllers, is written in English.
-   **Input Forms:** Dedicated form objects for handling and validating incoming data.
-   **Mappers:** Manual mappers for clean and efficient conversion between entities, forms, and DTOs.
-   **Specifications:** JPA Specifications for advanced and flexible filtering capabilities.
-   **Docker Support:** Fully containerized with Docker and Docker Compose for easy setup and deployment.

## Features

-   **User Management:** Create and manage users.
-   **Task Management:** Create, update, and filter tasks.
-   **Subtask Management:** Create, update, and manage subtasks associated with a main task.
-   **Status Tracking:** Track the status of tasks and subtasks (PENDING, IN_PROGRESS, COMPLETED).
-   **Advanced Filtering:** Filter tasks by status, user, or title using JPA Specifications.
-   **Pagination and Sorting:** Paginate and sort results for all list endpoints.
-   **Validation:** Robust validation for all incoming data.
-   **Centralized Exception Handling:** Standardized error responses for all exceptions.
-   **API Documentation:** Interactive API documentation with Swagger/OpenAPI.
-   **Containerization:** Ready to run with Docker and Docker Compose.

## Technologies

-   **Java 17:** Core programming language.
-   **Spring Boot 3.1.5:** Application framework.
-   **Spring Data JPA:** For data persistence and repository management.
-   **Hibernate:** JPA implementation.
-   **PostgreSQL:** Production-ready relational database.
-   **H2 Database:** In-memory database for testing.
-   **Lombok:** To reduce boilerplate code.
-   **Maven:** Dependency management and build tool.
-   **Swagger/OpenAPI:** For API documentation.
-   **Docker & Docker Compose:** For containerization and orchestration.
-   **JUnit 5 & Mockito:** For unit and integration testing.

## Getting Started

### Prerequisites

-   Java 17
-   Maven 3.9+
-   Docker & Docker Compose

### Running with Docker (Recommended)

This is the easiest way to get the application and all its services running.

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd task-management-api
    ```

2.  **Build and run with Docker Compose:**

    ```bash
    docker-compose up --build
    ```

    This command will:
    -   Build the Spring Boot application into a JAR file.
    -   Create a Docker image for the application.
    -   Start containers for the API, a PostgreSQL database, Redis, and an Nginx reverse proxy.

3.  **Access the API:**

    -   **API Base URL:** `http://localhost/api`
    -   **Swagger UI:** `http://localhost/swagger-ui.html`

### Running Locally

If you prefer to run the application directly on your machine:

1.  **Compile and package the application:**

    ```bash
    mvn clean install
    ```

2.  **Run the application:**

    ```bash
    java -jar target/task-management-api-1.0-SNAPSHOT.jar
    ```

    The application will start on `http://localhost:8080` and connect to an in-memory H2 database by default.

## API Endpoints

All endpoints are available under the `/api` prefix. For a complete and interactive list of endpoints, please visit the [Swagger UI](http://localhost/swagger-ui.html) once the application is running.

### Users

-   `POST /users`: Create a new user.
-   `GET /users/{id}`: Find a user by ID.
-   `GET /users/email/{email}`: Find a user by email.

### Tasks

-   `POST /tasks`: Create a new task.
-   `GET /tasks`: List tasks with filtering and pagination.
-   `GET /tasks/{id}`: Find a task by ID.
-   `PATCH /tasks/{id}/status`: Update the status of a task.

### Subtasks

-   `POST /tasks/{taskId}/subtasks`: Create a new subtask for a task.
-   `GET /tasks/{taskId}/subtasks`: List all subtasks for a task.
-   `GET /subtasks/{id}`: Find a subtask by ID.
-   `PATCH /subtasks/{id}/status`: Update the status of a subtask.

## Project Structure

```
.task-management-api
├── docker
│   ├── init.sql
│   └── nginx.conf
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/ipaas/taskmanagement
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── exception
│   │   │       ├── form
│   │   │       ├── mapper
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── specification
│   │   └── resources
│   └── test
├── .dockerignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

-   **`config`**: OpenAPI configuration.
-   **`controller`**: REST controllers for handling API requests.
-   **`dto`**: Data Transfer Objects for API responses.
-   **`entity`**: JPA entities representing the database schema.
-   **`exception`**: Custom exceptions and global exception handler.
-   **`form`**: Input forms for request body validation.
-   **`mapper`**: Mappers for converting between entities, DTOs, and forms.
-   **`repository`**: Spring Data JPA repositories.
-   **`service`**: Business logic layer.
-   **`specification`**: JPA Specifications for dynamic queries.
-   **`docker`**: Configuration files for Docker services (PostgreSQL, Nginx).
-   **`Dockerfile`**: Instructions for building the application's Docker image.
-   **`docker-compose.yml`**: Defines and orchestrates the multi-container Docker application.

'''
