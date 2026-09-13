# Healthcare Patient Management System

Backend REST API for managing patients, doctors, and appointments in a healthcare setting. This project is built with Java, Spring Boot, Spring Data JPA, PostgreSQL, Bean Validation, Spring Security, OpenAPI documentation, Docker, and Docker Compose.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- H2 for tests
- Maven Wrapper
- Spring Security
- SpringDoc OpenAPI
- Docker
- Docker Compose

## Current Features

- Create, list, view, update, and delete patients
- Search patients by first or last name
- Create, list, view, update, and delete doctors
- Create, list, view, update, and delete appointments
- Connect appointments to patients and doctors
- Filter appointments by patient, doctor, or status
- Request validation
- Consistent API error responses
- Swagger UI enabled for API exploration
- Docker setup for the API and PostgreSQL
- Actuator health endpoint for deployment health checks
- GitHub Actions CI pipeline

## PostgreSQL Setup

Create a local database named `healthcare_pms`:

```sql
CREATE DATABASE healthcare_pms;
```

The app uses these defaults:

```text
DB_URL=jdbc:postgresql://localhost:5432/healthcare_pms
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

If your PostgreSQL password is different, set the environment variable `DB_PASSWORD` before running the app.

## Run The App Locally

```powershell
.\mvnw.cmd spring-boot:run
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Run With Docker Compose

```powershell
docker compose up --build
```

The API runs on:

```text
http://localhost:8080
```

Docker Compose starts PostgreSQL on host port `5433`.

## Run Tests

```powershell
.\mvnw.cmd test
```

## CI/CD Pipeline

This project uses GitHub Actions to validate every push and pull request to `main`.

The CI workflow:

- Checks out the repository
- Sets up Java 21
- Runs `./mvnw clean verify`
- Uploads the packaged application JAR as a build artifact
- Builds the Docker image
- Starts a PostgreSQL container
- Starts the API container connected to PostgreSQL
- Verifies the API is healthy at `/actuator/health`
- Cleans up the Docker containers and network

The pipeline confirms that the application can compile, pass automated tests, package into a JAR, build as a Docker image, start with PostgreSQL, and respond to a health check.

## Example Patient Request

```json
{
  "firstName": "Maria",
  "lastName": "Garcia",
  "dateOfBirth": "1990-04-12",
  "gender": "FEMALE",
  "email": "maria.garcia@example.com",
  "phoneNumber": "555-123-4567",
  "address": "123 Main St, Dallas, TX"
}
```

## Next Milestones

- Add integration tests for doctor and appointment endpoints
- Deploy to AWS with PostgreSQL hosted in RDS
- Add role-based access for admin, doctor, and receptionist users
