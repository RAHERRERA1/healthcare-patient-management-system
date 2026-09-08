# Healthcare Patient Management System

Backend API for managing patients in a healthcare setting. This project is built with Java, Spring Boot, Spring Data JPA, PostgreSQL, Bean Validation, Spring Security, and OpenAPI documentation.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- H2 for tests
- Maven Wrapper
- SpringDoc OpenAPI

## Current Features

- Create patients
- List patients
- Search patients by first or last name
- View one patient
- Update patients
- Delete patients
- Request validation
- Consistent API error responses
- Swagger UI enabled for API exploration

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

## Run the App

```powershell
.\mvnw.cmd spring-boot:run
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Run Tests

```powershell
.\mvnw.cmd test
```

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

- Add doctor/provider management
- Add appointment scheduling
- Add medical records and visit notes
- Add role-based access for admin, doctor, and receptionist users
- Add integration tests for patient API endpoints
