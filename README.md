# Healthcare Patient Management System

A backend REST API for managing patients, doctors, and appointments in a healthcare setting. This project was built with Java, Spring Boot, PostgreSQL, Docker, and Docker Compose as a learning project for healthcare technology backend roles.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Docker
- Docker Compose
- Maven

## Features

- Patient CRUD operations
- Doctor CRUD operations
- Appointment CRUD operations
- Patient-to-appointment relationship
- Doctor-to-appointment relationship
- Appointment filtering by patient, doctor, and status
- Appointment status enum validation
- Request and response DTOs
- Service layer business logic
- Global exception handling
- PostgreSQL persistence
- Dockerized Spring Boot API and PostgreSQL database

## Project Structure

```text
src/main/java/com/raul/healthcare
|-- appointment
|   |-- Appointment.java
|   |-- AppointmentController.java
|   |-- AppointmentNotFoundException.java
|   |-- AppointmentRepository.java
|   |-- AppointmentRequest.java
|   |-- AppointmentResponse.java
|   |-- AppointmentService.java
|   |-- AppointmentStatus.java
|-- doctor
|   |-- Doctor.java
|   |-- DoctorController.java
|   |-- DoctorNotFoundException.java
|   |-- DoctorRepository.java
|   |-- DoctorRequest.java
|   |-- DoctorResponse.java
|   |-- DoctorService.java
|-- exception
|   |-- ErrorResponse.java
|   |-- GlobalExceptionHandler.java
|-- patient
    |-- Patient.java
    |-- PatientController.java
    |-- PatientNotFoundException.java
    |-- PatientRepository.java
    |-- PatientRequest.java
    |-- PatientResponse.java
    |-- PatientService.java
```

## Database Model

The application uses three main tables:

- `patients`
- `doctors`
- `appointments`

Relationships:

```text
patients.id  -> appointments.patient_id
doctors.id   -> appointments.doctor_id
```

An appointment belongs to one patient and one doctor.

## Appointment Status Values

Appointments use an enum for status:

```text
SCHEDULED
COMPLETED
CANCELLED
RESCHEDULED
```

Invalid values are rejected by the API.

## API Endpoints

### Patients

```http
GET    /api/patients/test
GET    /api/patients
GET    /api/patients/{id}
POST   /api/patients
PUT    /api/patients/{id}
DELETE /api/patients/{id}
```

### Doctors

```http
GET    /api/doctors
GET    /api/doctors/{id}
POST   /api/doctors
PUT    /api/doctors/{id}
DELETE /api/doctors/{id}
```

### Appointments

```http
GET    /api/appointments
GET    /api/appointments/{id}
POST   /api/appointments
PUT    /api/appointments/{id}
DELETE /api/appointments/{id}
GET    /api/appointments/patient/{patientId}
GET    /api/appointments/doctor/{doctorId}
GET    /api/appointments/status/{status}
```

## Example Requests

### Create Patient

```http
POST /api/patients
```

```json
{
  "firstName": "Raul",
  "lastName": "Hernandez",
  "dateOfBirth": "1998-05-15"
}
```

### Create Doctor

```http
POST /api/doctors
```

```json
{
  "firstName": "Sarah",
  "lastName": "Miller",
  "specialty": "Cardiology",
  "email": "sarah.miller@clinic.com"
}
```

### Create Appointment

```http
POST /api/appointments
```

```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2026-08-20T14:00:00",
  "reason": "Follow-up visit",
  "status": "SCHEDULED"
}
```

## Validation Examples

The API validates incoming requests. Examples of invalid data include:

- Blank patient or doctor names
- Invalid email format
- Missing required fields
- Appointment date in the past
- Invalid appointment status

Example validation response:

```json
{
  "errors": {
    "appointmentDateTime": "must be a date in the present or in the future"
  },
  "message": "Validation Failed",
  "status": 400,
  "timestamp": "2026-07-14T14:26:14.4715323"
}
```

## Running With Docker

This project includes Docker support for both the Spring Boot API and PostgreSQL.

Start the application:

```powershell
docker compose up -d --build
```

Check running containers:

```powershell
docker ps
```

View API logs:

```powershell
docker logs healthcare-api
```

Stop the application:

```powershell
docker compose down
```

The PostgreSQL data is stored in a Docker volume named:

```text
healthcare_postgres_data
```

This keeps database data available after containers are stopped and restarted.

## Local Configuration

The application uses environment-variable fallbacks in `application.properties`:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/healthcare_pms}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
```

When running with Docker Compose, the API container connects to PostgreSQL using:

```text
jdbc:postgresql://postgres:5432/healthcare_pms
```

When running from IntelliJ, the app can connect through:

```text
jdbc:postgresql://localhost:5433/healthcare_pms
```

## Development Commands

Run tests locally:

```powershell
.\mvnw.cmd test
```

Build the project locally:

```powershell
.\mvnw.cmd clean package
```

## Future Improvements

- Add scheduling conflict prevention
- Add Spring Security authentication and authorization
- Add user roles such as admin, doctor, and staff
- Add automated controller/service tests
- Add database migrations with Flyway or Liquibase
- Deploy to AWS using RDS and ECS or Elastic Beanstalk
- Add CI/CD with GitHub Actions
- Move secrets to AWS Secrets Manager for production

## Learning Goals

This project demonstrates backend development skills useful for healthcare technology roles, including REST API design, layered Spring Boot architecture, relational database modeling, validation, error handling, Docker, and deployment preparation.

